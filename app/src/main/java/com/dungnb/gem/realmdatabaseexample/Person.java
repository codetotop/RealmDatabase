package com.dungnb.gem.realmdatabaseexample;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Person extends RealmObject {
  @PrimaryKey
  private String id;
  private String name;
  private RealmList<Dog> dogs; // Declare one-to-many relationships

  public Person() {
  }

  public Person(String id, String name, RealmList<Dog> dogs) {
    this.id = id;
    this.name = name;
    if (dogs == null)
      dogs = new RealmList<>();
    else
      this.dogs = dogs;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public RealmList<Dog> getDogs() {
    return dogs;
  }

  public void setDogs(RealmList<Dog> dogs) {
    this.dogs = dogs;
  }

  @Override
  public String toString() {
    return "Person{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", dogs=" + dogsTostring() +
        '}';
  }

  private String dogsTostring() {
    String dogListToString = "";
    for (Dog dog : dogs) {
      dogListToString += dog.toString();
    }
    return dogListToString;
  }

  // ... Generated getters and setters ...
}