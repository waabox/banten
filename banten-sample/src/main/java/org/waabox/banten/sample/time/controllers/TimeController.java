package org.waabox.banten.sample.time.controllers;

import javax.transaction.Transactional;

import org.elasticsearch.common.lang3.Validate;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;
import org.waabox.banten.sample.time.domain.*;

/** The time controller.
 * @author waabox (waabox[at]gmail[dot]com)
 */
@RestController
@RequestMapping("/time")
public class TimeController {

  /** The time repository. */
  private final TimeRepository repository;

  /** Creates a new instance of the controller.
   * @param theTimeRepository the time repository.
   */
  public TimeController(final TimeRepository theTimeRepository) {
    Validate.notNull(theTimeRepository, "Time repository cannot be null");
    repository = theTimeRepository;
  }

  @Transactional
  @RequestMapping(value = "/view.html", method = RequestMethod.GET)
  public ModelAndView view() {
    ModelAndView mav = new ModelAndView("view");
    mav.addObject("times", repository.getTimes());
    return mav;
  }

  @Transactional
  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public ModelAndView save(@RequestParam("gmt") final String gmt) {
    Time time = new Time(gmt);
    repository.save(time);
    return new ModelAndView("redirect:view.html");
  }

}
