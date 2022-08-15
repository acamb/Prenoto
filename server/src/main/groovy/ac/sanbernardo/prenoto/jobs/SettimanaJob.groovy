package ac.sanbernardo.prenoto.jobs

import ac.sanbernardo.prenoto.aop.Logged
import ac.sanbernardo.prenoto.services.PrenotazioneService
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Inject
import jakarta.inject.Singleton

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
