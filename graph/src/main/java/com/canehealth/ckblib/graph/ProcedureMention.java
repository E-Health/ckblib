package com.canehealth.ckblib.graph;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Node("Procedure")
@AllArgsConstructor
public class ProcedureMention {
    @Id
    private final String cui;

    @Getter
    private final String name;

    @Relationship(type = "HAS_ATTRIBUTES", direction = OUTGOING)
    private List<ConceptAttributes> attributes = new ArrayList<>();

    @Relationship(value = "TREATED_WITH", direction = INCOMING)
    private final List<BaseRelation> diseases;
}
