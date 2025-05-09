import clsx from 'clsx'
import PropTypes from 'prop-types'
import React from 'react'
import { uncontrollable } from 'uncontrollable'
import {
  accessor,
  views as componentViews,
  dateFormat,
  dateRangeFormat,
  DayLayoutAlgorithmPropType,
} from './utils/propTypes'

import { mergeWithDefaults } from './localizer'
import NoopWrapper from './NoopWrapper'
import Toolbar from './Toolbar'
import { navigate, views } from './utils/constants'
import { notify } from './utils/helpers'
import message from './utils/messages'
import moveDate from './utils/move'
import VIEWS from './Views'

import defaults from 'lodash/defaults'
import mapValues from 'lodash/mapValues'
import omit from 'lodash/omit'
import transform from 'lodash/transform'
import { wrapAccessor } from './utils/accessors'

function viewNames(_views) {
  if (Array.isArray(_views)) {
    return _views
  }
  const views = []
  for (const [key, value] of Object.entries(_views)) {
    if (value) {
      views.push(key)
    }
  }
  return views
}

function isValidView(view, { views: _views }) {
  let names = viewNames(_views)
  return names.indexOf(view) !== -1
}

class Calendar extends React.Component {
  static propTypes = {

    localizer: PropTypes.object.isRequired,

    elementProps: PropTypes.object,

    date: PropTypes.instanceOf(Date),

    view: PropTypes.string,
    
    defaultView: PropTypes.string,
    
    events: PropTypes.arrayOf(PropTypes.object),

    backgroundEvents: PropTypes.arrayOf(PropTypes.object),

    titleAccessor: accessor,

    tooltipAccessor: accessor,

    allDayAccessor: accessor,

    startAccessor: accessor,

    endAccessor: accessor,

    eventIdAccessor: accessor,

    resourceAccessor: accessor,

    resources: PropTypes.arrayOf(PropTypes.object),

    resourceIdAccessor: accessor,

    resourceTitleAccessor: accessor,

    /**
     * Determines the current date/time which is highlighted in the views.
     *
     * The value affects which day is shaded and which time is shown as
     * the current time. It also affects the date used by the Today button in
     * the toolbar.
     *
     * Providing a value here can be useful when you are implementing time zones
     * using the `startAccessor` and `endAccessor` properties.
     *
     * @type {func}
     * @default () => new Date()
     */
    getNow: PropTypes.func,

    /**
     * Callback fired when the `date` value changes.
     *
     * @controllable date
     */
    onNavigate: PropTypes.func,

    /**
     * Callback fired when the `view` value changes.
     *
     * @controllable view
     */
    onView: PropTypes.func,

    /**
     * Callback fired when date header, or the truncated events links are clicked
     *
     */
    onDrillDown: PropTypes.func,

    /**
     *
     * ```js
     * (dates: Date[] | { start: Date; end: Date }, view: 'month'|'week'|'work_week'|'day'|'agenda'|undefined) => void
     * ```
     *
     * Callback fired when the visible date range changes. Returns an Array of dates
     * or an object with start and end dates for BUILTIN views. Optionally new `view`
     * will be returned when callback called after view change.
     *
     * Custom views may return something different.
     */
    onRangeChange: PropTypes.func,

    onSelectSlot: PropTypes.func,

    onSelectEvent: PropTypes.func,

    onDoubleClickEvent: PropTypes.func,

    onKeyPressEvent: PropTypes.func,

    onSelecting: PropTypes.func,
    
    onShowMore: PropTypes.func,

    showAllEvents: PropTypes.bool,

    /**
     * The selected event, if any.
     */
    selected: PropTypes.object,
    
    views: componentViews,

    /**
     * Determines whether the drill down should occur when clicking on the "+_x_ more" link.
     * If `popup` is false, and `doShowMoreDrillDown` is true, the drill down will occur as usual.
     * If `popup` is false, and `doShowMoreDrillDown` is false, the drill down will not occur and the `onShowMore` function will trigger.
     */
    doShowMoreDrillDown: PropTypes.bool,

    /**
     * The string name of the destination view for drill-down actions, such
     * as clicking a date header, or the truncated events links. If
     * `getDrilldownView` is also specified it will be used instead.
     *
     * Set to `null` to disable drill-down actions.
     *
     * ```js
     * <Calendar
     *   drilldownView="agenda"
     * />
     * ```
     */
    drilldownView: PropTypes.string,

    /**
     * Functionally equivalent to `drilldownView`, but accepts a function
     * that can return a view name. It's useful for customizing the drill-down
     * actions depending on the target date and triggering view.
     *
     * Return `null` to disable drill-down actions.
     *
     * ```js
     * <Calendar
     *   getDrilldownView={(targetDate, currentViewName, configuredViewNames) =>
     *     if (currentViewName === 'month' && configuredViewNames.includes('week'))
     *       return 'week'
     *
     *     return null;
     *   }}
     * />
     * ```
     */
    getDrilldownView: PropTypes.func,

    /**
     * Determines the end date from date prop in the agenda view
     * date prop + length (in number of days) = end date
     */
    length: PropTypes.number,

    /**
     * Determines whether the toolbar is displayed
     */
    toolbar: PropTypes.bool,

    /**
     * Show truncated events in an overlay when you click the "+_x_ more" link.
     */
    popup: PropTypes.bool,

    /**
     * Distance in pixels, from the edges of the viewport, the "show more" overlay should be positioned.
     *
     * ```jsx
     * <Calendar popupOffset={30}/>
     * <Calendar popupOffset={{x: 30, y: 20}}/>
     * ```
     */
    popupOffset: PropTypes.oneOfType([
      PropTypes.number,
      PropTypes.shape({ x: PropTypes.number, y: PropTypes.number }),
    ]),

    /**
     * Allows mouse selection of ranges of dates/times.
     *
     * The 'ignoreEvents' option prevents selection code from running when a
     * drag begins over an event. Useful when you want custom event click or drag
     * logic
     */
    selectable: PropTypes.oneOf([true, false, 'ignoreEvents']),

    longPressThreshold: PropTypes.number,

    /**
     * Determines the selectable time increments in week and day views, in minutes.
     */
    step: PropTypes.number,

    /**
     * The number of slots per "section" in the time grid views. Adjust with `step`
     * to change the default of 1 hour long groups, with 30 minute slots.
     */
    timeslots: PropTypes.number,

    /**
     *Switch the calendar to a `right-to-left` read direction.
     */
    rtl: PropTypes.bool,

    eventPropGetter: PropTypes.func,

    slotPropGetter: PropTypes.func,

    slotGroupPropGetter: PropTypes.func,

    dayPropGetter: PropTypes.func,

    showMultiDayTimes: PropTypes.bool,

    allDayMaxRows: PropTypes.number,

    /**
     * Constrains the minimum _time_ of the Day and Week views.
     */
    min: PropTypes.instanceOf(Date),

    /**
     * Constrains the maximum _time_ of the Day and Week views.
     */
    max: PropTypes.instanceOf(Date),

    /**
     * Determines how far down the scroll pane is initially scrolled down.
     */
    scrollToTime: PropTypes.instanceOf(Date),

    /**
     * Determines whether the scroll pane is automatically scrolled down or not.
     */
    enableAutoScroll: PropTypes.bool,

    /**
     * Determines the layout of resource groups in the calendar.
     * When `true`, resources will be grouped by date in the week view.
     * When `false`, resources will be grouped by week.
     */
    resourceGroupingLayout: PropTypes.bool,

    /**
     * Specify a specific culture code for the Calendar.
     *
     * **Note: it's generally better to handle this globally via your i18n library.**
     */
    culture: PropTypes.string,
    formats: PropTypes.shape({
      /**
       * Format for the day of the month heading in the Month view.
       * e.g. "01", "02", "03", etc
       */
      dateFormat,

      /**
       * A day of the week format for Week and Day headings,
       * e.g. "Wed 01/04"
       *
       */
      dayFormat: dateFormat,

      /**
       * Week day name format for the Month week day headings,
       * e.g: "Sun", "Mon", "Tue", etc
       *
       */
      weekdayFormat: dateFormat,

      /**
       * The timestamp cell formats in Week and Time views, e.g. "4:00 AM"
       */
      timeGutterFormat: dateFormat,

      /**
       * Toolbar header format for the Month view, e.g "2015 April"
       *
       */
      monthHeaderFormat: dateFormat,

      /**
       * Toolbar header format for the Week views, e.g. "Mar 29 - Apr 04"
       */
      dayRangeHeaderFormat: dateRangeFormat,

      /**
       * Toolbar header format for the Day view, e.g. "Wednesday Apr 01"
       */
      dayHeaderFormat: dateFormat,

      /**
       * Toolbar header format for the Agenda view, e.g. "4/1/2015 – 5/1/2015"
       */
      agendaHeaderFormat: dateRangeFormat,

      /**
       * A time range format for selecting time slots, e.g "8:00am – 2:00pm"
       */
      selectRangeFormat: dateRangeFormat,

      agendaDateFormat: dateFormat,
      agendaTimeFormat: dateFormat,
      agendaTimeRangeFormat: dateRangeFormat,

      /**
       * Time range displayed on events.
       */
      eventTimeRangeFormat: dateRangeFormat,

      /**
       * An optional event time range for events that continue onto another day
       */
      eventTimeRangeStartFormat: dateFormat,

      /**
       * An optional event time range for events that continue from another day
       */
      eventTimeRangeEndFormat: dateFormat,
    }),
    components: PropTypes.shape({
      event: PropTypes.elementType,
      eventWrapper: PropTypes.elementType,
      eventContainerWrapper: PropTypes.elementType,
      dateCellWrapper: PropTypes.elementType,
      dayColumnWrapper: PropTypes.elementType,
      timeSlotWrapper: PropTypes.elementType,
      timeGutterHeader: PropTypes.elementType,
      timeGutterWrapper: PropTypes.elementType,
      resourceHeader: PropTypes.elementType,
      showMore: PropTypes.elementType,

      toolbar: PropTypes.elementType,

      agenda: PropTypes.shape({
        date: PropTypes.elementType,
        time: PropTypes.elementType,
        event: PropTypes.elementType,
      }),

      day: PropTypes.shape({
        header: PropTypes.elementType,
        event: PropTypes.elementType,
      }),
      week: PropTypes.shape({
        header: PropTypes.elementType,
        event: PropTypes.elementType,
      }),
      month: PropTypes.shape({
        header: PropTypes.elementType,
        dateHeader: PropTypes.elementType,
        event: PropTypes.elementType,
      }),
    }),
    messages: PropTypes.shape({
      allDay: PropTypes.node,
      previous: PropTypes.node,
      next: PropTypes.node,
      today: PropTypes.node,
      month: PropTypes.node,
      week: PropTypes.node,
      day: PropTypes.node,
      agenda: PropTypes.node,
      date: PropTypes.node,
      time: PropTypes.node,
      event: PropTypes.node,
      noEventsInRange: PropTypes.node,
      showMore: PropTypes.func,
    }),
    dayLayoutAlgorithm: DayLayoutAlgorithmPropType,
  }

  static defaultProps = {
    events: [],
    backgroundEvents: [],
    elementProps: {},
    popup: false,
    toolbar: true,
    view: views.MONTH,
    views: [views.MONTH, views.WEEK, views.DAY, views.AGENDA],
    step: 30,
    length: 30,
    allDayMaxRows: Infinity,

    doShowMoreDrillDown: true,
    drilldownView: views.DAY,

    titleAccessor: 'title',
    tooltipAccessor: 'title',
    allDayAccessor: 'allDay',
    startAccessor: 'start',
    endAccessor: 'end',
    resourceAccessor: 'resourceId',

    resourceIdAccessor: 'id',
    resourceTitleAccessor: 'title',

    eventIdAccessor: 'id',

    longPressThreshold: 250,
    getNow: () => new Date(),
    dayLayoutAlgorithm: 'overlap',
  }

  constructor(...args) {
    super(...args)

    this.state = {
      context: Calendar.getContext(this.props),
    }
  }
  static getDerivedStateFromProps(nextProps) {
    return { context: Calendar.getContext(nextProps) }
  }

  static getContext({
    startAccessor,
    endAccessor,
    allDayAccessor,
    tooltipAccessor,
    titleAccessor,
    resourceAccessor,
    resourceIdAccessor,
    resourceTitleAccessor,
    eventIdAccessor,
    eventPropGetter,
    backgroundEventPropGetter,
    slotPropGetter,
    slotGroupPropGetter,
    dayPropGetter,
    view,
    views,
    localizer,
    culture,
    messages = {},
    components = {},
    formats = {},
  }) {
    let names = viewNames(views)
    const msgs = message(messages)
    return {
      viewNames: names,
      localizer: mergeWithDefaults(localizer, culture, formats, msgs),
      getters: {
        eventProp: (...args) =>
          (eventPropGetter && eventPropGetter(...args)) || {},
        backgroundEventProp: (...args) =>
          (backgroundEventPropGetter && backgroundEventPropGetter(...args)) ||
          {},
        slotProp: (...args) =>
          (slotPropGetter && slotPropGetter(...args)) || {},
        slotGroupProp: (...args) =>
          (slotGroupPropGetter && slotGroupPropGetter(...args)) || {},
        dayProp: (...args) => (dayPropGetter && dayPropGetter(...args)) || {},
      },
      components: defaults(components[view] || {}, omit(components, names), {
        eventWrapper: NoopWrapper,
        backgroundEventWrapper: NoopWrapper,
        eventContainerWrapper: NoopWrapper,
        dateCellWrapper: NoopWrapper,
        weekWrapper: NoopWrapper,
        timeSlotWrapper: NoopWrapper,
        timeGutterWrapper: NoopWrapper,
      }),
      accessors: {
        start: wrapAccessor(startAccessor),
        end: wrapAccessor(endAccessor),
        allDay: wrapAccessor(allDayAccessor),
        tooltip: wrapAccessor(tooltipAccessor),
        title: wrapAccessor(titleAccessor),
        resource: wrapAccessor(resourceAccessor),
        resourceId: wrapAccessor(resourceIdAccessor),
        resourceTitle: wrapAccessor(resourceTitleAccessor),
        eventId: wrapAccessor(eventIdAccessor),
      },
    }
  }

  getViews = () => {
    const views = this.props.views

    if (Array.isArray(views)) {
      return transform(views, (obj, name) => (obj[name] = VIEWS[name]), {})
    }

    if (typeof views === 'object') {
      return mapValues(views, (value, key) => {
        if (value === true) {
          return VIEWS[key]
        }

        return value
      })
    }

    return VIEWS
  }

  getView = () => {
    const views = this.getViews()

    return views[this.props.view]
  }

  getDrilldownView = (date) => {
    const { view, drilldownView, getDrilldownView } = this.props

    if (!getDrilldownView) return drilldownView

    return getDrilldownView(date, view, Object.keys(this.getViews()))
  }

  render() {
    let {
      view,
      toolbar,
      events,
      backgroundEvents,
      resourceGroupingLayout,
      style,
      className,
      elementProps,
      date: current,
      getNow,
      length,
      showMultiDayTimes,
      onShowMore,
      doShowMoreDrillDown,
      components: _0,
      formats: _1,
      messages: _2,
      culture: _3,
      ...props
    } = this.props

    current = current || getNow()

    let View = this.getView()
    const { accessors, components, getters, localizer, viewNames } =
      this.state.context

    let CalToolbar = components.toolbar || Toolbar
    const label = View.title(current, { localizer, length })

    return (
      <div
        {...elementProps}
        className={clsx(className, 'rbc-calendar', props.rtl && 'rbc-rtl')}
        style={style}
      >
        {toolbar && (
          <CalToolbar
            date={current}
            view={view}
            views={viewNames}
            label={label}
            onView={this.handleViewChange}
            onNavigate={this.handleNavigate}
            localizer={localizer}
          />
        )}
        <View
          {...props}
          events={events}
          backgroundEvents={backgroundEvents}
          date={current}
          getNow={getNow}
          length={length}
          localizer={localizer}
          getters={getters}
          components={components}
          accessors={accessors}
          showMultiDayTimes={showMultiDayTimes}
          getDrilldownView={this.getDrilldownView}
          onNavigate={this.handleNavigate}
          onDrillDown={this.handleDrillDown}
          onSelectEvent={this.handleSelectEvent}
          onDoubleClickEvent={this.handleDoubleClickEvent}
          onKeyPressEvent={this.handleKeyPressEvent}
          onSelectSlot={this.handleSelectSlot}
          onShowMore={onShowMore}
          doShowMoreDrillDown={doShowMoreDrillDown}
          resourceGroupingLayout={resourceGroupingLayout}
        />
      </div>
    )
  }
  handleRangeChange = (date, viewComponent, view) => {
    let { onRangeChange, localizer } = this.props

    if (onRangeChange) {
      if (viewComponent.range) {
        onRangeChange(viewComponent.range(date, { localizer }), view)
      } else {
        if (process.env.NODE_ENV !== 'production') {
          console.error('onRangeChange prop not supported for this view')
        }
      }
    }
  }

  handleNavigate = (action, newDate) => {
    let { view, date, getNow, onNavigate, ...props } = this.props
    let ViewComponent = this.getView()
    let today = getNow()

    date = moveDate(ViewComponent, {
      ...props,
      action,
      date: newDate || date || today,
      today,
    })

    onNavigate(date, view, action)
    this.handleRangeChange(date, ViewComponent)
  }

  handleViewChange = (view) => {
    if (view !== this.props.view && isValidView(view, this.props)) {
      this.props.onView(view)
    }

    let views = this.getViews()
    this.handleRangeChange(
      this.props.date || this.props.getNow(),
      views[view],
      view
    )
  }

  handleSelectEvent = (...args) => {
    notify(this.props.onSelectEvent, args)
  }

  handleDoubleClickEvent = (...args) => {
    notify(this.props.onDoubleClickEvent, args)
  }

  handleKeyPressEvent = (...args) => {
    notify(this.props.onKeyPressEvent, args)
  }

  handleSelectSlot = (slotInfo) => {
    notify(this.props.onSelectSlot, slotInfo)
  }

  handleDrillDown = (date, view) => {
    const { onDrillDown } = this.props
    if (onDrillDown) {
      onDrillDown(date, view, this.drilldownView)
      return
    }
    if (view) this.handleViewChange(view)

    this.handleNavigate(navigate.DATE, date)
  }
}

export default uncontrollable(Calendar, {
  view: 'onView',
  date: 'onNavigate',
  selected: 'onSelectEvent',
})
