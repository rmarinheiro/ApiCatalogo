package br.com.rafael.catalogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.rafael.catalogo.entities.Category;
import br.com.rafael.catalogo.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

}
