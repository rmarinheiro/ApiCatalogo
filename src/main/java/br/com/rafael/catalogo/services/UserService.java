package br.com.rafael.catalogo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.rafael.catalogo.dto.RoleDTO;
import br.com.rafael.catalogo.dto.UserDTO;
import br.com.rafael.catalogo.dto.UserInsertDTO;
import br.com.rafael.catalogo.dto.UserUpdateDTO;
import br.com.rafael.catalogo.entities.Role;
import br.com.rafael.catalogo.entities.User;
import br.com.rafael.catalogo.repository.RoleRepository;
import br.com.rafael.catalogo.repository.UserRepository;
import br.com.rafael.catalogo.services.exceptions.DataBaseException;
import br.com.rafael.catalogo.services.exceptions.EntityResourceNotFoundException;

@Service
public class UserService implements UserDetailsService {
	
	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncorder;
	
	

	@Transactional(readOnly = true)
	public List<UserDTO> findAll() {
		List<User> lista = userRepository.findAll();
		List<UserDTO> listaDto = lista.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());

		return listaDto;

	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> obj = userRepository.findById(id);
		User user = obj.orElseThrow(() -> new EntityResourceNotFoundException("Entity Not Found"));
		

		return new UserDTO(user);
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User entity = new User();
		// entity.setName(dto.getName());
		copyDTOToEntity(dto, entity);
		entity.setPassword(passwordEncorder.encode(dto.getPassword()));
		entity = userRepository.save(entity);
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO update(UserUpdateDTO dto, Long id) {
		try 	{
			User user = userRepository.getById(id);
			copyDTOToEntity(dto, user);
			user = userRepository.save(user);
			return new UserDTO(user);
		} catch (EntityNotFoundException e) {
			throw new EntityResourceNotFoundException("Id não encontrado");
		}
	}

	public void delete(Long id) {
		try {
			userRepository.deleteById(id);
		} catch (EntityNotFoundException e) {
			throw new EntityResourceNotFoundException("Id não Existe");
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Erro ao deletar a categoria");
		}

	}

	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(PageRequest pageRequest) {
		Page<User> listaPaginada = userRepository.findAll(pageRequest);
		return listaPaginada.map(x -> new UserDTO(x));
	}

	private void copyDTOToEntity(UserDTO dto, User user) {
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		user.setEmail(dto.getEmail());
		//user.setPassword(dto.get)
		user.getRoles().clear();
		for(RoleDTO roleDTO : dto.getRoles()) {
			Role role = roleRepository.getById(roleDTO.getId());
			user.getRoles().add(role);
		}
		
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user= userRepository.findByEmail(username);
		if(user == null) {
			logger.error("User not found: " + username);
			throw new UsernameNotFoundException("Email não encontrado");
		}
		logger.info("User found: " + username);
		return user;
	}

}
