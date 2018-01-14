package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MainGenerator {
    public static void main(String[] args)
            throws Exception
    {
        final Schema       schema;
        final Entity       nameEntity;
        final DaoGenerator generator;

        schema = new Schema(1, "comp3717.bcit.ca.daodatabase.database.schema");
        nameEntity = schema.addEntity("School");
        nameEntity.addIdProperty();
        nameEntity.addStringProperty("name").notNull();
        nameEntity.addDoubleProperty("longitude").notNull();
        nameEntity.addDoubleProperty("latitude").notNull();
        nameEntity.addIntProperty("level").notNull();
        generator = new DaoGenerator();
        generator.generateAll(schema, "./app/src/main/java");
    }
}
