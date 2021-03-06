package org.mycash.web.api;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.mycash.domain.Usuario;
import org.mycash.service.UsuarioService;
import org.mycash.web.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService service;
	
	@Autowired
	private ModelMapper mapper;
	
	@GetMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	List<UsuarioDTO> todos() {
		List<Usuario> usuarios = service.findAll();
		List<UsuarioDTO> usuariosDTO = usuarios
			.stream()
			.map((u) -> mapper.map(u, UsuarioDTO.class))
			.collect(Collectors.toList());
		
		return usuariosDTO;
	}
	
	@PostMapping
	Usuario criar(
			@RequestParam(required = true) String email, 
			@RequestParam(required = true) String senha) {
		
		return service.registraUsuarioUser(email, senha);
	}
	
	@GetMapping("/{email}")
	@PreAuthorize("#email == authentication.getName() or hasRole('ROLE_ADMIN')")
	Usuario apenasUm(@PathVariable("email") String email) {
		return service.findByEmail(email);
	}
	
	@PutMapping("/{email}/senha")
	@PreAuthorize("#email == authentication.getName()")
	Usuario trocarSenha(
			@PathVariable("email") String email, 
			@RequestParam(required = true) String senhaAntiga, 
			@RequestParam(required = true) String senhaNova) {
	
		return service.trocarSenha(email, senhaAntiga, senhaNova);
	}
	
	@PutMapping("/{email}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Usuario resetarSenha(
			@PathVariable("email") String email, 
			@RequestParam(required = true) String senhaNova) {
	
		return service.resetarSenha(email, senhaNova);
	}
	
}