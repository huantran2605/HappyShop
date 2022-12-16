package com.happyshop.common.entity.setting;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "settings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Setting {

    @Id
    @Column(name="`key`", nullable = false, length = 128)
    private String key;
    @Column(nullable = false, length = 1024)
    private String value;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 45)
    private SettingCategory category;
 
    public Setting(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Setting other = (Setting) obj;
        return Objects.equals(key, other.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
    
}
