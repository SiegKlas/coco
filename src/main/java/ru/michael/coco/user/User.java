package ru.michael.coco.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.michael.coco.level.Level;
import ru.michael.coco.task.Task;
import ru.michael.coco.topic.Topic;

import java.util.Collection;
import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Table(name = "_user")
public class User implements UserDetails {
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private final String username;
    private String password;
    private Long teacherId = 1L;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Topic> topics;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Level> levels;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Task> tasks;

    public enum Role {
        USER, ADMIN
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
