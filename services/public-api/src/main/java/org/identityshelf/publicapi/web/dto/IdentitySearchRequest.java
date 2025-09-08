package org.identityshelf.publicapi.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.List;
import java.util.Map;

@Schema(description = "Identity search request")
public class IdentitySearchRequest {
    
    @Schema(description = "General search query")
    @JsonProperty("query")
    private String query;
    
    @Schema(description = "Filter by identity type")
    @JsonProperty("type")
    private String type;
    
    @Schema(description = "Filter by identity status", allowableValues = {"ACTIVE", "INACTIVE", "SUSPENDED", "PENDING"})
    @JsonProperty("status")
    private String status;
    
    @Schema(description = "Filter by attribute values")
    @JsonProperty("attributes")
    private Map<String, Object> attributes;
    
    @Schema(description = "Filter by identifiers")
    @JsonProperty("identifiers")
    private List<IdentifierSearchCriteria> identifiers;
    
    @Schema(description = "Page number (0-based)", example = "0", minimum = "0")
    @Min(value = 0, message = "Page must be non-negative")
    @JsonProperty("page")
    private Integer page = 0;
    
    @Schema(description = "Page size", example = "20", minimum = "1", maximum = "100")
    @Min(value = 1, message = "Size must be at least 1")
    @Max(value = 100, message = "Size must not exceed 100")
    @JsonProperty("size")
    private Integer size = 20;
    
    @Schema(description = "Sort field", example = "createdAt", allowableValues = {"displayName", "createdAt", "updatedAt"})
    @JsonProperty("sort")
    private String sort = "createdAt";
    
    @Schema(description = "Sort order", example = "DESC", allowableValues = {"ASC", "DESC"})
    @JsonProperty("order")
    private String order = "DESC";
    
    // Constructors
    public IdentitySearchRequest() {}
    
    // Getters and setters
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Map<String, Object> getAttributes() { return attributes; }
    public void setAttributes(Map<String, Object> attributes) { this.attributes = attributes; }
    
    public List<IdentifierSearchCriteria> getIdentifiers() { return identifiers; }
    public void setIdentifiers(List<IdentifierSearchCriteria> identifiers) { this.identifiers = identifiers; }
    
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
    
    public String getSort() { return sort; }
    public void setSort(String sort) { this.sort = sort; }
    
    public String getOrder() { return order; }
    public void setOrder(String order) { this.order = order; }
}
