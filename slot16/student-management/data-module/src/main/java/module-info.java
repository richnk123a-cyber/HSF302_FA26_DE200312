open module vonguyenkhanh.example.datamodule {
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires spring.data.jpa;
    requires spring.data.commons;
    requires spring.context;
    requires spring.tx;

    exports vonguyenkhanh.example.datamodule.pojo;
    exports vonguyenkhanh.example.datamodule.repository;
    exports vonguyenkhanh.example.datamodule.service;
    exports vonguyenkhanh.example.datamodule.exception;
}
