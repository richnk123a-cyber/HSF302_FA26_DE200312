module vonguyenkhanh.example.datamodule {

    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires spring.data.jpa;
    requires spring.data.commons;
    requires spring.context;

    exports vonguyenkhanh.example.datamodule.pojo;
}