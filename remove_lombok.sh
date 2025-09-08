#!/bin/bash

# Function to remove Lombok annotations and add basic getters/setters
remove_lombok_from_entity() {
    local file="$1"
    local class_name=$(basename "$file" .java)
    
    echo "Processing $class_name..."
    
    # Remove Lombok imports and annotations
    sed -i '/import lombok/d' "$file"
    sed -i '/@Data/d' "$file"
    sed -i '/@NoArgsConstructor/d' "$file"
    sed -i '/@AllArgsConstructor/d' "$file"
    sed -i '/@Getter/d' "$file"
    sed -i '/@Setter/d' "$file"
    
    # Add basic constructor if not present
    if ! grep -q "public $class_name()" "$file"; then
        # Find the class declaration and add constructor after it
        sed -i "/public class $class_name {/a\\
\\
    public $class_name() {}\\
" "$file"
    fi
}

# Process all entity files
for file in shared/identity-data/src/main/java/org/identityshelf/data/entity/*.java; do
    if grep -q "lombok\|@Data\|@NoArgsConstructor\|@AllArgsConstructor" "$file"; then
        remove_lombok_from_entity "$file"
    fi
done

echo "Lombok removal completed!"
