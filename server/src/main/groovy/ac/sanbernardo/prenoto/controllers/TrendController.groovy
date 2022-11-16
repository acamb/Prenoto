package ac.sanbernardo.prenoto.controllers

import ac.sanbernardo.prenoto.model.dto.DailyTrend
import ac.sanbernardo.prenoto.services.TrendService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import jakarta.inject.Inject

import javax.annotation.security.RolesAllowed

@Controller("/api/trend")
@Secured(SecurityRule.IS_AUTHENTICATED)
class TrendController {

    @Inject
    TrendService service

    @Get("/day/{day}")
    public DailyTrend dailyTrend(int day){
        return service.getDailyTrend(day)
    }
}
