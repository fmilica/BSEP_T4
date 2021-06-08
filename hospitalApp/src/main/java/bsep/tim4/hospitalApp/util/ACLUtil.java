package bsep.tim4.hospitalApp.util;

import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.nio.file.attribute.AclEntryFlag.DIRECTORY_INHERIT;
import static java.nio.file.attribute.AclEntryFlag.FILE_INHERIT;
import static java.nio.file.attribute.AclEntryPermission.*;

public class ACLUtil {
    public static void setupACL(String folderPath) throws IOException {
        //File folder = new File("./logs");
        File folder = new File(folderPath);
        for (File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                // provera koji je sistem u pitanju -> Windows vs Unix
                if (SystemUtils.IS_OS_WINDOWS) {
                    // Windows ACL
                    Path path = Paths.get(fileEntry.getPath());
                    GroupPrincipal administrators = path.getFileSystem().getUserPrincipalLookupService()
                            .lookupPrincipalByGroupName("Users");
                    AclFileAttributeView view = Files.getFileAttributeView(path, AclFileAttributeView.class);
                    AclEntry adminEntry = AclEntry.newBuilder()
                            .setType(AclEntryType.ALLOW)
                            .setPrincipal(administrators)
                            .setFlags(DIRECTORY_INHERIT,
                                    FILE_INHERIT)
                            .setPermissions(SYNCHRONIZE,
                                    READ_DATA,
                                    EXECUTE,
                                    APPEND_DATA,
                                    READ_ATTRIBUTES,
                                    READ_NAMED_ATTRS,
                                    READ_ACL,
                                    WRITE_ATTRIBUTES,
                                    WRITE_OWNER,
                                    WRITE_NAMED_ATTRS,
                                    READ_NAMED_ATTRS,
                                    WRITE_DATA)
                            .build();

                    List<AclEntry> aclEntries = new ArrayList<>();
                    aclEntries.add(adminEntry);
                    view.setAcl(aclEntries);
                } else {
                    // UNIX
                    Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxr-xr--");
                    Files.setPosixFilePermissions(Paths.get(fileEntry.getPath()), permissions);
                }
            }
        }
    }
}
