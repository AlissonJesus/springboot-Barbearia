package com.barbearia.barbadeodin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.barbearia.barbadeodin.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
