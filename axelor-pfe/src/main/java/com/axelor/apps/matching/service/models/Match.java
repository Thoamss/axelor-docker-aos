package com.axelor.apps.matching.service.models;

public class Match {

  public int id;
  public String title;
  public int applicant;
  public int opportunity;
  public int matchRate;

  public Match() {}

  public Match(String t, int app, int opp, int mtchr) {
    this.title = t;
    this.applicant = app;
    this.opportunity = opp;
    this.matchRate = mtchr;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getApplicant() {
    return applicant;
  }

  public void setApplicant(int applicant) {
    this.applicant = applicant;
  }

  public int getOpportunity() {
    return opportunity;
  }

  public void setOpportunity(int opportunity) {
    this.opportunity = opportunity;
  }

  public int getMatchRate() {
    return matchRate;
  }

  public void setMatchRate(int matchRate) {
    this.matchRate = matchRate;
  }
}
