package com.autodm.server.controller;

import com.autodm.server.dto.CharacterResourceDto;
import com.autodm.server.dto.PlayerCharacterDto;
import com.autodm.server.service.PlayerCharacterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PlayerCharacterControllerTest {

    @Mock
    private PlayerCharacterService characterService;

    @InjectMocks
    private PlayerCharacterController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCharactersByCampaign() {
        PlayerCharacterDto dto1 = new PlayerCharacterDto();
        dto1.setId(1L);
        PlayerCharacterDto dto2 = new PlayerCharacterDto();
        dto2.setId(2L);
        when(characterService.getCharactersByCampaignId(1L)).thenReturn(Arrays.asList(dto1, dto2));

        ResponseEntity<List<PlayerCharacterDto>> response = controller.getCharactersByCampaign(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getCharacter_Success() {
        PlayerCharacterDto dto = new PlayerCharacterDto();
        dto.setId(1L);
        when(characterService.getCharacterById(1L)).thenReturn(dto);

        ResponseEntity<PlayerCharacterDto> response = controller.getCharacter(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void getCharacter_NotFound() {
        when(characterService.getCharacterById(1L)).thenReturn(null);

        ResponseEntity<PlayerCharacterDto> response = controller.getCharacter(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createCharacter() {
        PlayerCharacterDto input = new PlayerCharacterDto();
        PlayerCharacterDto output = new PlayerCharacterDto();
        output.setId(1L);
        when(characterService.createCharacter(any())).thenReturn(output);

        ResponseEntity<PlayerCharacterDto> response = controller.createCharacter(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void updateCharacter_Success() {
        PlayerCharacterDto input = new PlayerCharacterDto();
        PlayerCharacterDto output = new PlayerCharacterDto();
        output.setId(1L);
        when(characterService.updateCharacter(eq(1L), any())).thenReturn(output);

        ResponseEntity<PlayerCharacterDto> response = controller.updateCharacter(1L, input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void updateCharacter_NotFound() {
        when(characterService.updateCharacter(eq(1L), any())).thenThrow(new IllegalArgumentException("Character not found"));

        ResponseEntity<PlayerCharacterDto> response = controller.updateCharacter(1L, new PlayerCharacterDto());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteCharacter() {
        ResponseEntity<Void> response = controller.deleteCharacter(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(characterService, times(1)).deleteCharacter(1L);
    }

    @Test
    void getResourcesByCharacter() {
        CharacterResourceDto dto1 = new CharacterResourceDto();
        dto1.setId(1L);
        when(characterService.getResourcesByCharacterId(1L)).thenReturn(Arrays.asList(dto1));

        ResponseEntity<List<CharacterResourceDto>> response = controller.getResourcesByCharacter(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void createResource_Success() {
        CharacterResourceDto input = new CharacterResourceDto();
        CharacterResourceDto output = new CharacterResourceDto();
        output.setId(1L);
        when(characterService.createResource(eq(1L), any())).thenReturn(output);

        ResponseEntity<CharacterResourceDto> response = controller.createResource(1L, input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void createResource_NotFound() {
        when(characterService.createResource(eq(1L), any())).thenThrow(new IllegalArgumentException("Character not found"));

        ResponseEntity<CharacterResourceDto> response = controller.createResource(1L, new CharacterResourceDto());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateResource_Success() {
        CharacterResourceDto input = new CharacterResourceDto();
        CharacterResourceDto output = new CharacterResourceDto();
        output.setId(1L);
        when(characterService.updateResource(eq(1L), any())).thenReturn(output);

        ResponseEntity<CharacterResourceDto> response = controller.updateResource(1L, input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void updateResource_NotFound() {
        when(characterService.updateResource(eq(1L), any())).thenThrow(new IllegalArgumentException("Resource not found"));

        ResponseEntity<CharacterResourceDto> response = controller.updateResource(1L, new CharacterResourceDto());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteResource() {
        ResponseEntity<Void> response = controller.deleteResource(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(characterService, times(1)).deleteResource(1L);
    }
}
