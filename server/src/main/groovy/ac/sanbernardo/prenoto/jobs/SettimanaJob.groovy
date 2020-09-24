package ac.sanbernardo.prenoto.jobs

import ac.sanbernardo.prenoto.aop.Logged
import ac.sanbernardo.prenoto.services.PrenotazioneService
import io.micronaut.scheduling.annotation.Scheduled

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettimanaJob {

    @Inject
    PrenotazioneService prenotazioneService

    @Scheduled(cron = "0 0 20 ? * SUN")
    @Logged
    void creaNuovaSettimana(){
        prenotazioneService.nuovaSettimana()
    }
}
