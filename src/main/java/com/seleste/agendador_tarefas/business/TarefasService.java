package com.seleste.agendador_tarefas.business;

import com.seleste.agendador_tarefas.business.DTO.TarefasDTO;
import com.seleste.agendador_tarefas.business.mapper.TarefasConverter;
import com.seleste.agendador_tarefas.infrastructure.entity.TarefasEntity;
import com.seleste.agendador_tarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.seleste.agendador_tarefas.infrastructure.repository.TarefasRepository;
import com.seleste.agendador_tarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TarefasService {

    private final TarefasRepository tarefasRepository;
    private final TarefasConverter tarefasConverter;
    private final JwtUtil jwtUtil;


    public TarefasDTO gravarTarefa(String token, TarefasDTO tarefasDTO) {
        String email = jwtUtil.extractUsername(token.substring(7));
        tarefasDTO.setDataCriacao(LocalDateTime.now());
        tarefasDTO.setStatusNotificacaoEnum(StatusNotificacaoEnum.PENDENTE);
        tarefasDTO.setEmailUsuario(email);
        TarefasEntity tarefasEntity = tarefasConverter.paraTarefasEntity(tarefasDTO);
        TarefasEntity tarefasSalvas = tarefasRepository.save(tarefasEntity);
        return tarefasConverter.paraTarefasDto(tarefasSalvas);
    }
}
