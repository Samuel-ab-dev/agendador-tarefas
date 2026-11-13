package com.seleste.agendador_tarefas.business.mapper;

import com.seleste.agendador_tarefas.business.DTO.TarefasDTO;
import com.seleste.agendador_tarefas.infrastructure.entity.TarefasEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TarefasConverter {
    TarefasEntity paraTarefasEntity(TarefasDTO tarefasDTO);

    TarefasDTO paraTarefasDto(TarefasEntity tarefasEntity);

    List<TarefasEntity> paraListTarefasEntity(List<TarefasDTO> tarefasDTO);

    List<TarefasDTO> paraListTarefasDTO(List<TarefasEntity> tarefasEntity);
}
