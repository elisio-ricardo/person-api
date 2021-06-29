package one.digitalinnovation.personapi.service;


import one.digitalinnovation.personapi.dto.MessageResponseDTO;
import one.digitalinnovation.personapi.dto.request.PersonDTO;
import one.digitalinnovation.personapi.entity.Person;
import one.digitalinnovation.personapi.exception.PersonNotFoundException;
import one.digitalinnovation.personapi.mapper.PersonMapper;
import one.digitalinnovation.personapi.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class PersonService {
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    private PersonRepository personRepository;

    private final PersonMapper personMapper = PersonMapper.INSTANCE;

    public MessageResponseDTO createPerson( PersonDTO personDTO){
        Person personToSave = personMapper.toModel(personDTO);

        Person savedPerson = personRepository.save(personToSave);

        return createdMessageResponse(savedPerson.getId(), "Created Person with ID ");
    }

    public List<PersonDTO> listAll() {
        List<Person> allpeople = personRepository.findAll();
        return allpeople.stream()
                .map(personMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO findById(Long id) throws PersonNotFoundException {
       Person person = verifyIfExists(id);
        /*Optional<Person> optionalPerson = personRepository.findById(id);
          if(optionalPerson.isEmpty()){
           // throw new PersonNotFoundException(id);
      } */
        return personMapper.toDTO(person);
    }



    public void delete(Long id) throws PersonNotFoundException {
        verifyIfExists(id);
        personRepository.deleteById(id);
    }


    public MessageResponseDTO updateById(Long id, PersonDTO personDTO) throws PersonNotFoundException{
            verifyIfExists(id);

        Person personToupdate = personMapper.toModel(personDTO);

        Person updatePerson = personRepository.save(personToupdate);

        return createdMessageResponse(updatePerson.getId(), "Update Person with ID ");
    }

    private Person verifyIfExists(Long id) throws PersonNotFoundException {
        return personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
    }


    private MessageResponseDTO createdMessageResponse(Long id, String message) {
        return MessageResponseDTO
                .builder()
                .message(message + id)
                .build();
    }
}
