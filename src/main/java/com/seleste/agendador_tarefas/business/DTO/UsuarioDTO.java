package com.seleste.agendador_tarefas.business.DTO;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private String email;
    private String senha;
}
