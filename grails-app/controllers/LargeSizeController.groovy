package grails.upload

import java.io.File;
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.*
import org.springframework.web.multipart.*
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider


class largeSizeController {


    def BUCKET_NAME = ''
    private static final String ACCESS_KEY = ''
    private static final String SECRET_KEY = ''

    AmazonS3 s3Client = null


    largeSizeController()  {
                BasicAWSCredentials awsCreds = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
                s3Client = AmazonS3ClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                        .build();

            }

    def index(){
        render(view:'index', model: [ id: "lucas"  ] )
    }


    def upload(){

        String prefix = params?.prefix
        MultipartFile mfile = request.getFile("fileid")

        File file = null ;
        try {
            file = convertMultiPartToFile(mfile)
            if (prefix == null)
                prefix = ""
            String path = prefix + file.getName()   // the path in bucket you want to uplaod
            println path
            PutObjectRequest request = new PutObjectRequest( BUCKET_NAME , path, file );


            s3Client.putObject(
                    this.BUCKET_NAME,
                    path,
                    file
            );
            log.info("uploaded file $path in $BUCKET_NAME")

        }
        catch (Exception e){
            log.error(e.getMessage())
        }
        finally {
            if(file?.exists())
                file.delete()
        }

        render(view:'upload', model: [ msg: "uploaded"  ] )

    }



    private File convertMultiPartToFile(MultipartFile file) throws IOException {

        int readByteCount = 0;
        byte[] buffer = new byte[4096];
        File convFile = new File(file.getOriginalFilename());
        FileInputStream input =  file.getInputStream();
        FileOutputStream output = new FileOutputStream(convFile)
        while((readByteCount = input.read(buffer)) != -1) {
            output.write(buffer, 0, readByteCount);
        }
        output.close();
        return convFile;
    }


    
    
}
