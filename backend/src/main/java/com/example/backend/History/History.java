package com.example.backend.History;

import jakarta.persistence.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.backend.Jacobi.JacobiResult;
import com.example.backend.Jacobi.MatrixData;
import com.example.backend.User.User;

import java.util.List;

@Entity
@Table(name = "history")
public class History  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data", nullable = false)
    private String data;

    @Column(name = "result", nullable = false)
    private String result;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public History() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MatrixData getData() {
        return convertJsonToMatrixData(this.data);
    }

    public History setData(MatrixData matrixData) {
        String jsonData = convertMatrixDataToJson(matrixData);
        if (jsonData != null) {
            this.data = jsonData;
        } else {
            throw new IllegalArgumentException("Error converting MatrixData to JSON");
        }
        return this;
    }

    public List<JacobiResult> getResult() {
        return convertJsonToJacobiResult(this.result);
    }

    public History setResult(List<JacobiResult> jacobiResults) {
        String jsonResult = convertJacobiResultToJson(jacobiResults);
        if (jsonResult != null) {
            this.result = jsonResult;
        } else {
            throw new IllegalArgumentException("Error converting JacobiResult to JSON");
        }
        return this;
    }

    public User getUser() {
        return user;
    }

    public History setUser(User user) {
        this.user = user;
        return this;
    }
    private String convertMatrixDataToJson(MatrixData matrixData) {
        try {
            return objectMapper.writeValueAsString(matrixData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private MatrixData convertJsonToMatrixData(String jsonData) {
        try {
            return objectMapper.readValue(jsonData, MatrixData.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String convertJacobiResultToJson(List<JacobiResult> jacobiResults) {
        try {
            return objectMapper.writeValueAsString(jacobiResults);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<JacobiResult> convertJsonToJacobiResult(String jsonResult) {
        try {
            return objectMapper.readValue(jsonResult,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, JacobiResult.class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
