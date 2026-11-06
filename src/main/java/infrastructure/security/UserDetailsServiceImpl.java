package infrastructure.security;

import com.seleste.agendador_tarefas.business.DTO.UsuarioDTO;
import infrastructure.security.client.UsuarioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl {

    @Autowired
    private UsuarioClient usuarioClient;
    public UserDetails loadUserByUsernameToken(String email, String token) {

        UsuarioDTO usuarioDTO = usuarioClient.buscaUsuarioPorEmail(email, token);
        return User
                .withUsername(usuarioDTO.getEmail())
                .password(usuarioDTO.getSenha())
                .build();
    }
}
