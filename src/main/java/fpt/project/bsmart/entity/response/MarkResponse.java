package fpt.project.bsmart.entity.response;

import fpt.project.bsmart.entity.dto.MarkDto;
import fpt.project.bsmart.entity.dto.ModuleDto;


public class MarkResponse{
    private ModuleDto module;
    private MarkDto markDto;

    public ModuleDto getModule() {
        return module;
    }

    public void setModule(ModuleDto module) {
        this.module = module;
    }

    public MarkDto getMarkDto() {
        return markDto;
    }

    public void setMarkDto(MarkDto markDto) {
        this.markDto = markDto;
    }
}
