package ru.utmn.co2_emissions.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Profile("jpa")
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByEmailIgnoreCase(username);
        if (person == null) throw new UsernameNotFoundException(username);

        return User.withUsername(person.getEmail())
                .password(person.getPassword())
                .roles(person.getRole())
                .disabled(!person.isEnabled())
                .build();
    }
}