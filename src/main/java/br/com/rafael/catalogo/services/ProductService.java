package br.com.rafael.catalogo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.rafael.catalogo.dto.CategoryDTO;
import br.com.rafael.catalogo.dto.ProductDTO;
import br.com.rafael.catalogo.entities.Category;
import br.com.rafael.catalogo.entities.Product;
import br.com.rafael.catalogo.repository.CategoryRepository;
import br.com.rafael.catalogo.repository.ProductRepository;
import br.com.rafael.catalogo.services.exceptions.DataBaseException;
import br.com.rafael.catalogo.services.exceptions.EmptyResourceNotFoundException;
import br.com.rafael.catalogo.services.exceptions.EntityResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public List<ProductDTO> findAll() {
		List<Product> lista = productRepository.findAll();
		List<ProductDTO> listaDto = lista.stream().map(x -> new ProductDTO(x)).collect(Collectors.toList());

		return listaDto;

	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = productRepository.findById(id);
		Product product = obj.orElseThrow(() -> new EntityResourceNotFoundException("Entity Not Found"));
		
		

		return new ProductDTO(product, product.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		// entity.setName(dto.getName());
		copyDTOToEntity(dto, entity);
		entity = productRepository.save(entity);
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(ProductDTO dto, Long id) {
		try 	{
			Product product = productRepository.getById(id);
			copyDTOToEntity(dto, product);
			product = productRepository.save(product);
			return new ProductDTO(product);
		} catch (EntityNotFoundException e) {
			throw new EntityResourceNotFoundException("Id não encontrado");
		}
	}

	public void delete(Long id) {
		try {
			productRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EmptyResourceNotFoundException("Id não encontrado");
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Erro ao deletar a categoria");
		}

	}

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
		Page<Product> listaPaginada = productRepository.findAll(pageRequest);
		return listaPaginada.map(x -> new ProductDTO(x));
	}

	private void copyDTOToEntity(ProductDTO dto, Product product) {
		product.setName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setImgUrl(dto.getImgUrl());
		product.setDate(dto.getDate());
		product.setPrice(dto.getPrice());
		//product.getCategories().clear();
		for(CategoryDTO catDTO : dto.getListaCategories()) {
			Category category = categoryRepository.getById(catDTO.getId());
			product.getCategories().add(category);
		}
		
	}

}
