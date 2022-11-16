package ac.sanbernardo.prenoto.services

import ac.sanbernardo.prenoto.aop.Logged
import ac.sanbernardo.prenoto.model.Configurazione
import ac.sanbernardo.prenoto.model.dto.DailyTrend
import ac.sanbernardo.prenoto.repositories.ConfigurazioneRepository
import ac.sanbernardo.prenoto.repositories.SlotPrenotazioneRepository
import io.micronaut.cache.annotation.Cacheable
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class TrendService {

    @Inject
    SlotPrenotazioneRepository slotPrenotazioneRepository
    @Inject
    ConfigurazioneRepository configurazioneRepository

    @Logged
    @Cacheable(value = "daily-trend")
    DailyTrend getDailyTrend(int day){
        int postimax = configurazioneRepository.findByChiaveAndValidoTrue(Configurazione.ConfigTokens.POSTI_PER_ORA.name()).orElseThrow().valore.toInteger()
        DailyTrend trend = new DailyTrend()
        trend.day = day

        trend.data = slotPrenotazioneRepository.getHourlyTrends(day).collect{
            it.value = postimax - it.value
            return it
        }
        return trend
    }
}
