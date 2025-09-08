package org.identityshelf.publicapi.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Identity search response")
public class IdentitySearchResponse {
    
    @Schema(description = "List of identities")
    @JsonProperty("content")
    private List<IdentityResponse> content;
    
    @Schema(description = "Current page number")
    @JsonProperty("page")
    private Integer page;
    
    @Schema(description = "Page size")
    @JsonProperty("size")
    private Integer size;
    
    @Schema(description = "Total number of elements")
    @JsonProperty("totalElements")
    private Long totalElements;
    
    @Schema(description = "Total number of pages")
    @JsonProperty("totalPages")
    private Integer totalPages;
    
    @Schema(description = "Whether this is the first page")
    @JsonProperty("first")
    private Boolean first;
    
    @Schema(description = "Whether this is the last page")
    @JsonProperty("last")
    private Boolean last;
    
    // Constructors
    public IdentitySearchResponse() {}
    
    public IdentitySearchResponse(List<IdentityResponse> content, Integer page, Integer size, 
                                 Long totalElements, Integer totalPages, Boolean first, Boolean last) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.first = first;
        this.last = last;
    }
    
    // Getters and setters
    public List<IdentityResponse> getContent() { return content; }
    public void setContent(List<IdentityResponse> content) { this.content = content; }
    
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
    
    public Long getTotalElements() { return totalElements; }
    public void setTotalElements(Long totalElements) { this.totalElements = totalElements; }
    
    public Integer getTotalPages() { return totalPages; }
    public void setTotalPages(Integer totalPages) { this.totalPages = totalPages; }
    
    public Boolean getFirst() { return first; }
    public void setFirst(Boolean first) { this.first = first; }
    
    public Boolean getLast() { return last; }
    public void setLast(Boolean last) { this.last = last; }
}
