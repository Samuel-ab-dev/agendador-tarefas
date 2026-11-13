package com.seleste.agendador_tarefas.business;

import com.seleste.agendador_tarefas.business.DTO.TarefasDTO;
import com.seleste.agendador_tarefas.business.mapper.TarefaUpdateConverter;
import com.seleste.agendador_tarefas.business.mapper.TarefasConverter;
import com.seleste.agendador_tarefas.infrastructure.entity.TarefasEntity;
import com.seleste.agendador_tarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.seleste.agendador_tarefas.infrastructure.exceptions.ResourceNotFoundException;
import com.seleste.agendador_tarefas.infrastructure.repository.TarefasRepository;
import com.seleste.agendador_tarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefasService {

    private final TarefasRepository tarefasRepository;
    private final TarefasConverter tarefasConverter;
    private final TarefaUpdateConverter tarefaUpdateConverter;
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

    public List<TarefasDTO> buscaTarefasAgendadas(LocalDateTime dataInicial,
                                                  LocalDateTime dataFinal) {
        return tarefasConverter.paraListTarefasDTO(
                tarefasRepository.findByDataEventoBetweenAndStatusNotificacaoEnum(dataInicial,
                        dataFinal,
                        StatusNotificacaoEnum.PENDENTE)
        );
    }

    public List<TarefasDTO> buscaEmailUsuario(String token) {
        String email = jwtUtil.extractUsername(token.substring(7));
        List<TarefasEntity> tarefasEntityList = tarefasRepository.findByEmailUsuario(email);
        return tarefasConverter.paraListTarefasDTO(tarefasEntityList);
    }

    public void deletaTarefaPorId(String id) {
        tarefasRepository.deleteById(id);
    }

    public TarefasDTO alteraStatus(StatusNotificacaoEnum status, String id) {
        try {
            TarefasEntity tarefasEntity = tarefasRepository.findById(id).orElseThrow(() ->
                    new ResourceNotFoundException("Tarefa não encontrada"));
            tarefasEntity.setStatusNotificacaoEnum(status);
            tarefasEntity.setDataAlteracao(LocalDateTime.now());
            TarefasEntity tarefaAlterada = tarefasRepository.save(tarefasEntity);
            return tarefasConverter.paraTarefasDto(tarefaAlterada);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao alterar status da tarefa: " +
                    e.getCause());
        }
    }

    public TarefasDTO updateTarefas(TarefasDTO tarefasDTO, String id) {
        try {
            TarefasEntity tarefasEntity = tarefasRepository.findById(id).orElseThrow(() ->
                    new ResourceNotFoundException("Tarefa não encontrada"));
            tarefaUpdateConverter.updateTarefas(tarefasDTO, tarefasEntity);
            TarefasEntity tarefaAtualizada = tarefasRepository.save(tarefasEntity);
            return tarefasConverter.paraTarefasDto(tarefaAtualizada);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Erro ao alterar status da tarefa: " +
                    e.getCause());
        }
    }
}
