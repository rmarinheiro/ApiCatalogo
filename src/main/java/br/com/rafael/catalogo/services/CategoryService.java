package br.com.rafael.catalogo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.rafael.catalogo.dto.CategoryDTO;
import br.com.rafael.catalogo.entities.Category;
import br.com.rafael.catalogo.repository.CategoryRepository;
import br.com.rafael.catalogo.services.exceptions.DataBaseException;
import br.com.rafael.catalogo.services.exceptions.EntityNotFoundException;


@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		List<Category> lista =  categoryRepository.findAll();
		List<CategoryDTO> listaDto = lista.stream().map(x->new CategoryDTO(x)).collect(Collectors.toList());
		
		return listaDto;
		
	}
	
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj= categoryRepository.findById(id);
		Category category = obj.orElseThrow(()-> new EntityNotFoundException("Entity Not Found"));
		
		return new CategoryDTO(category);
	}


	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		entity= categoryRepository.save(entity);
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO update(CategoryDTO dto,Long id) {
		try {
			Category category = categoryRepository.getById(id);
			category.setName(dto.getName());
			category = categoryRepository.save(category);
			return new CategoryDTO(category);
		} catch (EntityNotFoundException e) {
			throw new EntityNotFoundException("Id não encontrado");
		}
	}

	
	public void delete(Long id) {
		try {
			categoryRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException("Id não Existe");
		}
		catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Erro ao deletar a categoria");
		}
		
		
	}


	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
		Page<Category> listaPaginada =  categoryRepository.findAll(pageRequest);
		return listaPaginada.map(x->new CategoryDTO(x));
	}



}
