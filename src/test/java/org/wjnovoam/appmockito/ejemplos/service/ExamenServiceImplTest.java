package org.wjnovoam.appmockito.ejemplos.service;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.wjnovoam.appmockito.ejemplos.Datos;
import org.wjnovoam.appmockito.ejemplos.models.Examen;
import org.wjnovoam.appmockito.ejemplos.repositories.ExamenRepository;
import org.wjnovoam.appmockito.ejemplos.repositories.ExamenRepositoryImpl;
import org.wjnovoam.appmockito.ejemplos.repositories.PreguntaRepository;
import org.wjnovoam.appmockito.ejemplos.repositories.PreguntaRepositoryImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {

    @Mock
    private ExamenRepositoryImpl examenRepository;
    @Mock
    private PreguntaRepositoryImpl preguntaRepository;

    @InjectMocks
    private ExamenServiceImpl examenService;

    @Captor
    ArgumentCaptor<Long> captor;

    /*@BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); //-->>> Habilitar el uso de anotaciones @Mock, @InjectMocks
        //examenRepository = mock(ExamenRepository.class);
        //preguntaRepository = mock(PreguntaRepository.class);
        //examenService = new ExamenServiceImpl(examenRepository, preguntaRepository);
    }*/

    @Test
    void findExamenPorNombre() throws Exception {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        Optional<Examen> examen = examenService.findExamenPorNombre("Lenguaje");

        assertTrue(examen.isPresent());
        assertEquals(5L,examen.get().getId());
        assertEquals("Lenguaje", examen.get().getNombre());
    }

    @Test
    void findExamenPorNombreListaVacia(){
        List<Examen> datos = Collections.emptyList();
        when(examenRepository.findAll()).thenReturn(datos);

        Optional<Examen> examen = examenService.findExamenPorNombre("Lenguaje");

        assertFalse(examen.isPresent());
    }


    @Test
    void testPreguntasExamen() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen  = examenService.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmetica"));

    }

    @Test
    void testPreguntasExamenVerify() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen  = examenService.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmetica"));
        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }

    @Test
    void testNoExisteExamenVerify() {
        //Given ->
        when(examenRepository.findAll()).thenReturn(Collections.emptyList());
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        //When
        Examen examen  = examenService.findExamenPorNombreConPreguntas("Matematicas");

        //Then
        assertNull(examen);
        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(5L);
    }

    @Test
    void testGuardarExamen() {

        //Given -Entorno de prueba
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        //Simular el agregado de un id automatico
        when(examenRepository.guardar(any(Examen.class))).then(new Answer<Examen>(){

            Long secuencia = 8L;

            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        });

        //When -> cuando
        Examen examen = examenService.guardar(newExamen);

        //Then -> Validamos
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Fisica", examen.getNombre());

        verify(examenRepository).guardar(any(Examen.class));
        verify(preguntaRepository).guardarVarias(anyList());
    }

    @Test
    void testManejoException() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);
        when(preguntaRepository.findPreguntasPorExamenId(isNull())).thenThrow(IllegalArgumentException.class);

        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            examenService.findExamenPorNombreConPreguntas("Matematicas");
        });

        assertEquals(IllegalArgumentException.class, exception.getClass());

        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(null);
    }

    @Test
    void testArgumentMatchers() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        examenService.findExamenPorNombreConPreguntas("Matematicas");

        verify(examenRepository).findAll();
        //verify(preguntaRepository).findPreguntasPorExamenId(argThat(arg -> arg != null && arg.equals(5L)));
        verify(preguntaRepository).findPreguntasPorExamenId(argThat(arg -> arg != null && arg >= 5L));
        //verify(preguntaRepository).findPreguntasPorExamenId(eq(5L));
    }

    @Test
    void testArgumentMatchers2() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        examenService.findExamenPorNombreConPreguntas("Matematicas");

        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(argThat(new MiArgsMatchers()));
    }

    @Test
    void testArgumentMatchers3() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES_ID_NEGATIVOS);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        examenService.findExamenPorNombreConPreguntas("Matematicas");

        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(argThat(new MiArgsMatchers()));
    }

    public static  class MiArgsMatchers implements ArgumentMatcher<Long>{

        private Long argument;

        @Override
        public boolean matches(Long argument) {
            this.argument = argument;
            return argument != null && argument > 0;
        }

        @Override
        public String toString() {
            return "Es paraun mensaje personalizado de error que" +
                    " imprime mockito encaso de que falle el test " + this.argument +
                    " debe ser un entero positivo";
        }
    }

    @Test
    void testArgumentCaptor() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        examenService.findExamenPorNombreConPreguntas("Matematicas");

        //ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(preguntaRepository).findPreguntasPorExamenId(captor.capture());
        assertEquals(5L, captor.getValue());
    }

    @Test
    void testDoThrow() {
        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);
        doThrow(IllegalArgumentException.class).when(preguntaRepository).guardarVarias(anyList());
        assertThrows(IllegalArgumentException.class, ()-> {
            examenService.guardar(examen);
        });
    }

    @Test
    void testDoAnswer() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
       //when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        doAnswer(invocationOnMock -> {
            Long id = invocationOnMock.getArgument(0);
            return id == 5L ? Datos.PREGUNTAS : Collections.emptyList();
        }).when(preguntaRepository).findPreguntasPorExamenId(anyLong());

        Examen examen = examenService.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("geometria"));
        assertEquals(5L, examen.getId());
        assertEquals("Matematicas", examen.getNombre());

        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }

    @Test
    void testDoAnswerGuardarExamen() {

        //Given -Entorno de prueba
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        doAnswer(new Answer<Examen>(){
            Long secuencia = 8L;

            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        }).when(examenRepository).guardar(any(Examen.class));

        //When -> cuando
        Examen examen = examenService.guardar(newExamen);

        //Then -> Validamos
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Fisica", examen.getNombre());

        verify(examenRepository).guardar(any(Examen.class));
        verify(preguntaRepository).guardarVarias(anyList());
    }

    @Test
    void testDoCallRealMethod() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        //when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        doCallRealMethod().when(preguntaRepository).findPreguntasPorExamenId(anyLong());

        Examen examen = examenService.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(5L, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
    }

    @Test
    void testSpy() {
        ExamenRepository examenRepository = spy(ExamenRepositoryImpl.class);
        PreguntaRepository preguntaRepository = spy(PreguntaRepositoryImpl.class);
        ExamenService examenService = new ExamenServiceImpl(examenRepository, preguntaRepository);

        List<String> preguntas = Arrays.asList("integrales");
        //when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        doReturn(preguntas).when(preguntaRepository).findPreguntasPorExamenId(anyLong());

        Examen examen = examenService.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5L, examen.getId());
        assertEquals("Matematicas", examen.getNombre());
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("integrales"));

    }
}