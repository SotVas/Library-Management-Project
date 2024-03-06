package project.library.entity;

import javax.persistence.*;


@Entity
@Table(name = "users")
public class User {
    private Integer id;

    private String lastname;
    private String firstname;
    private String password;
    private String username;
    private Integer enable;

    private Role roleByUserrole;





    public User(String lastname, String firstname,Integer enable, String password, String username, Role roleByUserrole) {
        this.enable = enable;
        this.lastname = lastname;
        this.firstname = firstname;
        this.password = password;
        this.username = username;
        this.roleByUserrole = roleByUserrole;

    }

    public User() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @Basic
    @Column(name = "lastname")
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Basic
    @Column(name = "firstname")
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Basic
    @Column(name = "enable")
    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }


    @ManyToOne
    @JoinColumn(name = "userrole", referencedColumnName = "role_id", nullable = false)
    public Role getRoleByUserrole() {
        return roleByUserrole;
    }

    public void setRoleByUserrole(Role roleByUserrole) {
        this.roleByUserrole = roleByUserrole;
    }
}
