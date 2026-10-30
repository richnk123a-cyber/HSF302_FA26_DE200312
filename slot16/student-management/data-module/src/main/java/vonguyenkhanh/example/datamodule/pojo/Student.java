package vonguyenkhanh.example.datamodule.pojo;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

// TODO 1.1: @Entity, @Table(name = "students")
@Entity
@Table(name = "students")
public class Student {

    // TODO 1.2: @Id + @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO 1.3: @Nationalized + @Column(nullable = false, length = 100)
    @Nationalized
    @Column(nullable = false, length = 100)
    private String name;

    // TODO 1.4: @Column(nullable = false, unique = true, length = 150)
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    // TODO 1.5: @Column(nullable = false)
    @Column(nullable = false)
    private Integer age;

    // TODO 1.6: Constructor rỗng
    public Student() {
    }

    // Constructor (name, email, age)
    public Student(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    // Constructor (id, name, email, age)
    public Student(Long id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    // TODO 1.7: Getter/Setter đủ 4 field
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    // TODO 1.8: toString() "id | name | email | age "
    @Override
    public String toString() {
        return id + " | " + name + " | " + email + " | " + age;
    }
}