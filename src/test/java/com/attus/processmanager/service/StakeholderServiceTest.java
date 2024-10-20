package com.attus.processmanager.service;

import com.attus.processmanager.models.Stakeholder;
import com.attus.processmanager.models.enums.StakeholderType;
import com.attus.processmanager.repository.StakeholderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StakeholderServiceTest {

    @InjectMocks
    private StakeholderService stakeholderService;

    @Mock
    private StakeholderRepository stakeholderRepository;

    @Test
    @DisplayName("Teste: Salvar parte interessada")
    void saveSuccess() {
        Stakeholder stakeholder = createStakeholder();
        Mockito.when(stakeholderService.save(stakeholder))
                .thenReturn(stakeholder);

        Stakeholder saved = stakeholderService.save(stakeholder);
        assertEquals(stakeholder, saved);
        Mockito.verify(stakeholderRepository).save(stakeholder);
    }

    @Test
    @DisplayName("Teste: Remover parte interessada")
    void remove() {
        Stakeholder stakeholder = createStakeholder();
        stakeholderService.remove(stakeholder);
        Mockito.verify(stakeholderRepository,
                Mockito.times(1))
                .delete(stakeholder);
    }

    @Test
    @DisplayName("Teste: Buscar parte interessada por id")
    void getById() {
        Stakeholder stakeholder = createStakeholder();
        Mockito.when(stakeholderRepository.findBy(stakeholder.getId())).thenReturn(stakeholder);
        Stakeholder foundStakeholder = stakeholderService.getById(stakeholder.getId());
        Assertions.assertNotNull(foundStakeholder);
    }

    @Test
    @DisplayName("Teste: Lança exceção ao buscar parte interessada por id inválido")
    void getByNonExistId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> stakeholderService.getById(-1L));
    }

    @Test
    @DisplayName("Teste: Listar todos as partes interessadas")
    void testList() {
        Stakeholder stakeholder = createStakeholder();
        Mockito.when(stakeholderRepository.findAll()).thenReturn(List.of(stakeholder));
        List<Stakeholder> list = stakeholderService.list(null);
        Assertions.assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Teste: Listar partes interessadas pelo tipo")
    void testListByType() {
        Stakeholder stakeholder = createStakeholder();
        stakeholder.setType(StakeholderType.AUTOR);

        Mockito.when(stakeholderRepository.listBy(StakeholderType.AUTOR)).thenReturn(List.of(stakeholder));
        List<Stakeholder> list = stakeholderService.list(StakeholderType.AUTOR);
        Assertions.assertEquals(1, list.size());
    }

    private Stakeholder createStakeholder() {
        return Stakeholder.builder()
                .id(1L)
                .email("test@tes.com")
                .phone("48484848")
                .type(StakeholderType.AUTOR)
                .build();
    }

}