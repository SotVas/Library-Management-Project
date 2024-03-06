package project.library.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="role_id")
    private Integer id;

    @Column(name="name")
    private String name;

    @OneToMany(mappedBy = "roleByUserrole")
    public Collection<User> usersById;

    public Role(Integer id, String name, Collection<User> usersById) {
        this.id = id;
        this.name = name;
        this.usersById = usersById;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", usersById=" + usersById +
                '}';
    }
}
