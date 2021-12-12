package br.com.rafael.catalogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.rafael.catalogo.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

}
