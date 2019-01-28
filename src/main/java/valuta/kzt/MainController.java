package valuta.kzt;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by BahaWood on 1/27/19.
 */

@Controller
@RequestMapping(path = "/valuta")
public class MainController {

    @Autowired
    private ValutaRepository valutaRepository;

    @Autowired
    private Scheduler scheduler;

    //Starting scheduler task
    @GetMapping(path = "/startScheduler")
    public @ResponseBody String startScheduler() {
        try {
            JobDetail jobDetail = JobBuilder.newJob(ValutaGettingJob.class)
                    .storeDurably()
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity("CronTrigger")
                    .withDescription("Dollar Course Parsing Trigger")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0/10 * 1/1 * ? *"))
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);

            return "Job started";
        } catch (SchedulerException e) {
            // schedule exception
            e.printStackTrace();
        }
        catch (Exception e){
            // other exception
            e.printStackTrace();
        }
        return "Error occured";
    }

    //Stop scheduler task
    @PostMapping(path = "/stopScheduler")
    public @ResponseBody String stopScheduler() {
        try {
            scheduler.shutdown();
            return "Task Stopped";
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return "Error";
    }

    //Get currency (fromTo = EUR/KZT) etc.
    @GetMapping(path = "/getCurrency")
    public @ResponseBody ValutaModel getCurrency(@RequestParam String fromTo) {
        return valutaRepository.findByFrom(fromTo);
    }
}
