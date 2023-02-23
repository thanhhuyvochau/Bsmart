package fpt.project.bsmart.util;

import fpt.project.bsmart.entity.Voice;
import fpt.project.bsmart.entity.response.VoiceResponse;
import fpt.project.bsmart.repository.VoiceRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VoiceUtil {
    private static VoiceRepository staticVoiceRepository;


    public VoiceUtil(VoiceRepository voiceRepository) {

        staticVoiceRepository = voiceRepository;

    }

    public static List<VoiceResponse> getVoice() {
        List<VoiceResponse> voiceResponses = new ArrayList<>();
        List<Voice> all = staticVoiceRepository.findAll();
        for (Voice voice : all) {
            voiceResponses.add(ObjectUtil.copyProperties(voice, new VoiceResponse(), VoiceResponse.class));
        }
        return voiceResponses;
    }

}
