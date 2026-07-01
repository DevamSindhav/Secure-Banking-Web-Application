package com.bank.unitedbank.repository;

import com.bank.unitedbank.entity.Customer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer , Long >{

	//some of the inbuilt methods
	//save(S entity) : saves the entity
	//findById()
	//findAll()  : returns list containing every row in table
	//existsById() : boolean for checking if a record exists
	//count() : return long count of total rows
	//deleteById() : removes the record from table
	//deleteAll()


	//returns customer by email
	//Optional is JDK 21 feature which helps writing cleaner and meaningful code
	Optional<Customer> findByEmail(String email);
	//the updates are all done in the service layers
	// an annotation called transactional will handle this



	//not writing methods for a specific column values (e.g. balance and all)
	//lets just transfer whole object and will use getter methods

	
}