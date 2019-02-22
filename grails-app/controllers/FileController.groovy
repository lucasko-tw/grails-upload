package grails.upload

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import groovy.util.logging.Slf4j

@Slf4j
class fileController {


    def BUCKET_NAME = ''
    private static final long MAX_ALLOWED_UPLOADFILE_SIZE = 1024 * 1024 * 1024 * 8l
    private static final String ACCESS_KEY = ''
    private static final String SECRET_KEY = ''


    def index(){

       def redirectUrl="${createLink(controller:'file', action: 'uploadOnSuccess', absolute: 'true')}"
       log.info "redirectUrl is $redirectUrl"
       def map= beforeUploadingFileByUser(params, redirectUrl)
       log.info "uploading $map ..."
        render(view:'index', model: [ id: "lucas" , aws:map ] )
    }

    def uploadOnSuccess()
    {
        render(view:'msg', model: [ msg: "success."] )
    }

    private beforeUploadingFileByUser(def params, def redirectUrl) {
        def values=[
                user: 'lucasko',
                params: params,
                redirectUrl: redirectUrl,
                category: "S3_FILE"
        ]
        def map=  createAmazonS3ObjectBeforeUploading(values)
        return map
    }


    private createAmazonS3ObjectBeforeUploading(Map values) {

        Map map = [:]
        map.key = ''
        def successActionRedirect = "${values.redirectUrl}?prefix=${map.key}"
        map = getPolicy( BUCKET_NAME , successActionRedirect)
        map.params = values.params
        map.success_action_redirect = successActionRedirect
        map.submitUrl = "https://${BUCKET_NAME}.s3.amazonaws.com"
        map.awsAccessKeyId= ACCESS_KEY


        return map
    }


    private getPolicy(def bucket,  def successActionRedirect) {

        def now = new Date() + 1
        def expiration = "${now.format('yyyy-MM-dd')}T${now.format('hh:mm:ss')}Z"
        def acl = "private"

        def text =
                """{"expiration": "$expiration", "conditions":
      [ 
       {"bucket": "$bucket"}, 
       ["starts-with", "\$key", ""],
       {"acl": "$acl"},
       {"success_action_redirect": "$successActionRedirect"},
       ["starts-with", "\$Content-Type", ""],
       ["content-length-range", 0, ${MAX_ALLOWED_UPLOADFILE_SIZE}]
     ]
    }"""

        text = text.replaceAll("\n", "").replaceAll("\r", "")
        def charset = "UTF-8"
        def algorithm = "HmacSHA1"

        def policy = text.getBytes(charset).encodeBase64().toString()

        def scretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(charset), algorithm)

        Mac hmac = Mac.getInstance(algorithm)
        hmac.init(scretKeySpec);

        String signature = hmac.doFinal(policy.getBytes(charset)).encodeBase64().toString()

        [policy: policy, signature: signature,  acl: acl, success_action_redirect: successActionRedirect]
    }
}
