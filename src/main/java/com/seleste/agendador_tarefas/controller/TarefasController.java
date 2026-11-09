package com.seleste.agendador_tarefas.controller;

import com.seleste.agendador_tarefas.business.DTO.TarefasDTO;
import com.seleste.agendador_tarefas.business.TarefasService;
import com.seleste.agendador_tarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.seleste.agendador_tarefas.infrastructure.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor
public class TarefasController {

    private final TarefasService tarefasService;

    @PostMapping
    public ResponseEntity<TarefasDTO> gravarTarefas(@RequestBody TarefasDTO tarefasDTO,
                                                    @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(tarefasService.gravarTarefa(token, tarefasDTO));
    }

    @GetMapping("/eventos")
    public ResponseEntity<List<TarefasDTO>> buscaListDeTarefas(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicial,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFinal) {

        return ResponseEntity.ok(tarefasService.buscaTarefasAgendadas(dataInicial, dataFinal));
    }

    @GetMapping
    public ResponseEntity<List<TarefasDTO>> buscaTarefasPorEmail(@RequestHeader("Authorization")
                                                                 String token) {
        return ResponseEntity.ok(tarefasService.buscaEmailUsuario(token));
    }

    @PatchMapping
    public ResponseEntity<TarefasDTO> alteraStatusDeNotificacao(@RequestParam("status")
                                                                StatusNotificacaoEnum status,
                                                                @RequestParam("id") String id) {
        return ResponseEntity.ok(tarefasService.alteraStatus(status, id));
    }

    @PutMapping
    public ResponseEntity<TarefasDTO> updateTarefas(@RequestBody TarefasDTO tarefasDTO,
                                                    @RequestParam("id") String id) {
        return ResponseEntity.ok(tarefasService.updateTarefas(tarefasDTO, id));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletaTarefaPorId(@RequestParam("id") String id) {
      try {
          tarefasService.deletaTarefaPorId(id);
      } catch (ResourceNotFoundException e) {
          throw new ResourceNotFoundException("Id não encontrado: " + id,
                  e.getCause());
      }
        return ResponseEntity.ok().build();
    }
}
