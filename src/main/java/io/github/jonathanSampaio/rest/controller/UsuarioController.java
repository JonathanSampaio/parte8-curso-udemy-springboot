package io.github.jonathanSampaio.rest.controller;

import io.github.jonathanSampaio.domain.entity.Usuario;
import io.github.jonathanSampaio.exception.SenhaInvalidaException;
import io.github.jonathanSampaio.rest.dto.CredenciaisDTO;
import io.github.jonathanSampaio.rest.dto.TokenDTO;
import io.github.jonathanSampaio.security.jwt.JwtService;
import io.github.jonathanSampaio.service.impl.UsuarioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioServiceImpl usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping
    @ResponseStatus(CREATED)
    public Usuario salvar(@RequestBody @Valid Usuario usuario){
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return usuarioService.salvar(usuario);
    }

    @PostMapping("/auth")
    @ResponseStatus(CREATED)
    public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais){

        try {
            Usuario usuario = Usuario.builder()
                    .login(credenciais.getLogin())
                    .senha(credenciais.getSenha()).build();
            UserDetails usuarioAutenticado = usuarioService.autenticar(usuario);
            String token = jwtService.gerarToken(usuario);

            return new TokenDTO(usuario.getLogin(), token);
        }catch (UsernameNotFoundException | SenhaInvalidaException  e){
            throw new ResponseStatusException(UNAUTHORIZED ,e.getMessage());
        }
    }


}
