package com.jdp.javademoproject.Person;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    // GET
    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        return new ResponseEntity<>(personRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        Optional<Person> person = personRepository.findById(id);

        return person.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST
    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        Person personCreated = personRepository.save(person);
        return new ResponseEntity<>(personCreated, HttpStatus.CREATED);
    }

    @PostMapping("/{name}")
    public ResponseEntity<Person> createPersonByName(@PathVariable String name, @RequestBody Person person) {
        person.setName(name);
        Person personCreated = personRepository.save(person);
            return new ResponseEntity<>(personCreated, HttpStatus.CREATED);
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody Person personDetails) {
        Optional<Person> person = personRepository.findById(id);

        if(person.isPresent()) {
            Person copyPerson = person.get();
            copyPerson.setName(personDetails.getName() != null ? personDetails.getName() : person.get().getName());
            copyPerson.setCity(personDetails.getCity() != null ? personDetails.getCity() : person.get().getCity());
            copyPerson.setPhoneNumber(personDetails.getPhoneNumber() != null ? personDetails.getPhoneNumber() : person.get().getPhoneNumber());

            Person updatedPerson = personRepository.save(copyPerson);
            return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        Optional<Person> person = personRepository.findById(id);

        if(person.isPresent()) {
            personRepository.delete(person.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

}
}
