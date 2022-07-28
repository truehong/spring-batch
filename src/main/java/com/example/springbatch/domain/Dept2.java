package com.example.springbatch.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Dept2 {

    @Id
    Integer depNo;
    String dName;
    String loc;
}
