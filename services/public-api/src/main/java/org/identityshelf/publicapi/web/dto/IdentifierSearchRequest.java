package org.identityshelf.publicapi.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.List;

@Schema(description = "Identifier search request")
public class IdentifierSearchRequest {
    
    @Schema(description = "Search query for identifier values")
    @JsonProperty("query")
    private String query;
    
    @Schema(description = "Filter by identifier types")
    @JsonProperty("types")
    private List<String> types;
    
    @Schema(description = "Filter by verification status")
    @JsonProperty("verified")
    private Boolean verified;
    
    @Schema(description = "Filter by primary status")
    @JsonProperty("primary")
    private Boolean primary;
    
    @Schema(description = "Page number (0-based)", example = "0", minimum = "0")
    @Min(value = 0, message = "Page must be non-negative")
    @JsonProperty("page")
    private Integer page = 0;
    
    @Schema(description = "Page size", example = "20", minimum = "1", maximum = "100")
    @Min(value = 1, message = "Size must be at least 1")
    @Max(value = 100, message = "Size must not exceed 100")
    @JsonProperty("size")
    private Integer size = 20;
    
    // Constructors
    public IdentifierSearchRequest() {}
    
    // Getters and setters
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    
    public List<String> getTypes() { return types; }
    public void setTypes(List<String> types) { this.types = types; }
    
    public Boolean getVerified() { return verified; }
    public void setVerified(Boolean verified) { this.verified = verified; }
    
    public Boolean getPrimary() { return primary; }
    public void setPrimary(Boolean primary) { this.primary = primary; }
    
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
}
