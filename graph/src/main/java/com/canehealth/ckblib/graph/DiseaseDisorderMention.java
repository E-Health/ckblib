package com.canehealth.ckblib.graph;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.stereotype.Component;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
@Node("Disease")
@Data
@Component
public class DiseaseDisorderMention {

    @Id
    private String cui;

    private String name;

    private List<JournalArticle> support = new ArrayList<>();

    @Relationship(type = "HAS_ATTRIBUTES", direction = OUTGOING)
    private List<ConceptAttributes> attributes = new ArrayList<>();

    // @Relationship(type = "HAS_SYMPTOM", direction = OUTGOING)
    // private Map<SignSymptomMention, WeightAttributes> symptoms = new HashMap<>();

    // @Relationship(type = "HAS_ANATOMY", direction = OUTGOING)
    // private Map<AnatomicalSiteMention, WeightAttributes> anatomy = new HashMap<>();

    // // Difficult to know if it is drug reaction or treatment
    // @Relationship(type = "IS_ASSOCIATED", direction = OUTGOING)
    // private Map<MedicationMention, WeightAttributes> treatments = new HashMap<>();

    // target is in differential diagnosis
    @Relationship(type = "HAS_ASSOCIATION", direction = OUTGOING)
    private List<DifferentialDiagnosis> dd = new ArrayList<>();

    // @Relationship(type = "HAS_PROCEDURE", direction = OUTGOING)
    // private Map<ProcedureMention, WeightAttributes> procedures = new HashMap<>();

}
