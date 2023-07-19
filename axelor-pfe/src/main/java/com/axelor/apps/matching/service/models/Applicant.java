package com.axelor.apps.matching.service.models;

import java.util.ArrayList;
import java.util.List;

public class Applicant {

  public int id;
  public List<Integer> roles;
  public List<Integer> comps;
  public String location;
  public int experience;
  public int salary;

  public Applicant() {
    this.roles = new ArrayList<Integer>();
    this.comps = new ArrayList<Integer>();
  }

  public Applicant(
      List<Integer> roles, List<Integer> comps, String location, int experience, int salary) {
    this.roles = roles;
    this.comps = comps;
    this.location = location;
    this.experience = experience;
    this.salary = salary;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<Integer> getRoles() {
    return roles;
  }

  public void setRoles(List<Integer> roles) {
    this.roles = roles;
  }

  public void addRole(int r) {
    this.roles.add(r);
  }

  public List<Integer> getComps() {
    return comps;
  }

  public void setComps(List<Integer> comps) {
    this.comps = comps;
  }

  public void addComps(int c) {
    this.comps.add(c);
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public int getExperience() {
    return experience;
  }

  public void setExperience(int experience) {
    this.experience = experience;
  }

  public int getSalary() {
    return salary;
  }

  public void setSalary(int salary) {
    this.salary = salary;
  }
}
