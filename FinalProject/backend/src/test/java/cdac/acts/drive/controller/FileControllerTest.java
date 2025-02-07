//package cdac.acts.drive.controller;
//
//
//import cdac.acts.drive.dto.FileFolderDTO;
//import cdac.acts.drive.entity.FileStorage;
//import cdac.acts.drive.entity.Folder;
//import cdac.acts.drive.entity.User;
//import cdac.acts.drive.repository.FileStorageRepository;
//import cdac.acts.drive.repository.FolderRepository;
//import cdac.acts.drive.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(MockitoExtension.class)
//class FileControllerTest {
//
//    private MockMvc mockMvc;
//
//    @InjectMocks
//    private FileController fileController;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private FolderRepository folderRepository;
//
//    @Mock
//    private FileStorageRepository fileStorageRepository;
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(fileController).build();
//    }
//
//    @Test
//    void testUploadFile_Success() throws Exception {
//        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("testUser");
//        Folder rootFolder = new Folder();
//        rootFolder.setId(1L);
//        rootFolder.setUser(user);
//
//        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
//        when(folderRepository.findByUserIdAndParentFolderIsNull(1L)).thenReturn(Optional.of(rootFolder));
//        when(fileStorageRepository.save(any(FileStorage.class))).thenReturn(new FileStorage());
//
//        mockMvc.perform(multipart("/api/files/upload")
//                        .file(file)
//                        .param("username", "testUser"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("File uploaded successfully"));
//    }
//
//    @Test
//    void testListFiles_Success() throws Exception {
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("testUser");
//        Folder rootFolder = new Folder();
//        rootFolder.setId(1L);
//        rootFolder.setUser(user);
//        FileStorage file = new FileStorage();
//        file.setId(1L);
//        file.setFileName("test.txt");
//        file.setFolder(rootFolder);
//
//        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
//        when(folderRepository.findByUserIdAndParentFolderIsNull(1L)).thenReturn(Optional.of(rootFolder));
//        when(fileStorageRepository.findByFolderId(1L)).thenReturn(List.of(file));
//
//        mockMvc.perform(post("/api/files/files")
//                        .param("username", "testUser"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].name").value("test.txt"));
//    }
//
//    @Test
//    void testDownloadFile_Success() throws Exception {
//        FileStorage fileStorage = new FileStorage();
//        fileStorage.setId(1L);
//        fileStorage.setFileName("test.txt");
//        Path tempFilePath = Files.createTempFile("test", ".txt");
//        Files.write(tempFilePath, "Hello, World!".getBytes());
//        fileStorage.setFilePath(tempFilePath.toString());
//
//        when(fileStorageRepository.findById(1L)).thenReturn(Optional.of(fileStorage));
//
//        mockMvc.perform(get("/api/files/download/1"))
//                .andExpect(status().isOk())
//                .andExpect(header().string("Content-Disposition", "attachment; filename=\"test.txt\""));
//    }
//}
//
