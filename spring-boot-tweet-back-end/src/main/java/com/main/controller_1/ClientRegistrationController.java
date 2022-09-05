//package com.main.controller_1;
//
//import com.main.model_1.ClientRegistration;
//import com.main.security.model.User;
//import com.main.security.service.impl.UserServiceImpl;
//import com.main.util.DateUtil;
//import com.main.util.EmailUtil;
//import com.microsoft.azure.servicebus.primitives.StringUtil;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.mail.MessagingException;
//import javax.validation.Valid;
//import javax.ws.rs.Produces;
//import java.text.ParseException;
//import java.time.Year;
//import java.util.Date;
//import java.util.List;
//
//@RestController
//@RequestMapping(value = "/lawfirm")
//@Tag(name = "Client Registration")
//@Slf4j
//public class ClientRegistrationController {
//
//    private ClientRegistrationServiceImpl registrationService;
//    private TransactionAllServiceImpl transactionService;
//    private PaymentInstallmentServiceImpl installmentService;
//    private UserServiceImpl userService;
//    private EmailUtil emailUtil;
//    private CaseInfoServiceImpl caseInfoService;
//    private AllDataSaveServiceImpl allDataSaveService;
//    private DateUtil dateUtil;
//
//    @Autowired
//    public ClientRegistrationController(final ClientRegistrationServiceImpl registrationService,
//                                        final TransactionAllServiceImpl transactionService,
//                                        final PaymentInstallmentServiceImpl installmentService,
//                                        final UserServiceImpl userService,
//                                        final EmailUtil emailUtil,
//                                        final CaseInfoServiceImpl caseInfoService,
//                                        final AllDataSaveServiceImpl allDataSaveService,
//                                        final DateUtil dateUtil) {
//        this.registrationService = registrationService;
//        this.transactionService = transactionService;
//        this.installmentService = installmentService;
//        this.userService = userService;
//        this.emailUtil = emailUtil;
//        this.caseInfoService = caseInfoService;
//        this.allDataSaveService = allDataSaveService;
//        this.dateUtil = dateUtil;
//    }
//
//    @Operation(summary = "This operation is used to save all data")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Creation of all data"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "500", description = "Exception occurred while serving the request")})
//    @PostMapping(value = "/saveAllData", produces = {"application/json"}, consumes = {"application/json"})
//    public ResponseEntity<ClientRegistration> saveAllData(@Valid @RequestBody ClientRegistration clientDetails) throws MessagingException {
//        log.info("Calling service for inserting data for all");
//
//        Integer serialNo = findSerialNumber().getBody();
//        String fileRefNo = "" + (serialNo + 11110) + "." + String.format("%ty", Year.now());
//        clientDetails.setSerialNo(serialNo);
//        clientDetails.setFileRefNo(fileRefNo);
//        // update few things for same client
//        ClientRegistration client = allDataSaveService.saveAllData(clientDetails);
//
//        if (client != null) {
//
//            User user = userService.findUser(clientDetails.getSystemUser());
//            if (user.getRoles().equals("ADMIN")) { // create folder now // otherwise activated time
//                if (clientDetails.getHasEmailSent().equals("Yes")
//                        && !StringUtil.isNullOrEmpty(clientDetails.getEmail())) // for email yes or no done in msg utitlity class
//                {
//                    emailUtil.sendEmailForNewFileCreation_for_admin(clientDetails);
//                }
//            } else {
//                emailUtil.sendEmailForFileActivationCode(clientDetails);
//            }
//        }
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(client);
//    }
//
//    @Operation(summary = "This operation is used to update client registration data")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Data is successfully updated"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "500", description = "Exception occurred while serving the request")})
//    @PutMapping(value = "/updateClient", produces = {"application/json"}, consumes = {"application/json"})
//    public ResponseEntity<ClientRegistration> updateClient(
//            @Parameter(description = "ClientRegistration Object", required = true) @Valid @RequestBody ClientRegistration clientDetails) {
//        log.info("Calling service for updating data");
//        // if fail then go in error
//        ClientRegistration client = registrationService.findByFileRefNo(clientDetails.getFileRefNo());
//
//        client.setStartDate(clientDetails.getStartDate());
//        client.setFeeEarnerCode(clientDetails.getFeeEarnerCode());
//        client.setFeeEarnerCodeWithImmigrationCode(clientDetails.getFeeEarnerCodeWithImmigrationCode());
//        client.setClientName(clientDetails.getClientName());
//        client.setClientOrCompanyDetails(clientDetails.getClientOrCompanyDetails());
//        client.setNiNumber(clientDetails.getNiNumber());
//        client.setDob(clientDetails.getDob());
//        client.setAddressLine(clientDetails.getAddressLine());
//        client.setCity(clientDetails.getCity());
//        client.setPostCode(clientDetails.getPostCode());
//        client.setPhoneNumber(clientDetails.getPhoneNumber());
//        client.setEmail(clientDetails.getEmail());
//        client.setAppCategory(clientDetails.getAppCategory());
//        client.setAppType(clientDetails.getAppType());
//        client.setCaseMatter(clientDetails.getCaseMatter());
//        client.setDescription(clientDetails.getDescription());
//        client.setPropertyAddress(clientDetails.getPropertyAddress());
//        client.setFeeType(clientDetails.getFeeType());
//        client.setPaymentMethod(clientDetails.getPaymentMethod());
//        client.setAgreedFee(clientDetails.getAgreedFee());
//        client.setModifiedBy(clientDetails.getModifiedBy());
//
//        registrationService.updateClientdetaills(client);
//
//        return ResponseEntity.status(HttpStatus.OK).body(client);
//    }
//
//    @Operation(summary = "This operation is used to activate client")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Data is successfully updated"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "500", description = "Exception occurred while serving the request")})
//    @PutMapping(value = "/updateClientUsingActivationCode",
//            produces = {"application/json"}, consumes = {"application/json"})
//    public ResponseEntity<ClientRegistration> updateClientUsingActivationCode(
//            @Parameter(description = "Client Object", required = true) @Valid @RequestBody ClientRegistration clientDetails) throws MessagingException {
//
//        log.info("Calling service for find client using file serial number");
//        ClientRegistration client =
//                registrationService.findBySerialNo(clientDetails.getSerialNo());
//
//        if (client != null) {
//            client.setIsActive("Active");
//            client.setModifiedBy(clientDetails.getModifiedBy());
//            registrationService.updateClientdetaillsForActivationCode(client);
//
//            User user = userService.findUser(client.getSystemUser());
//            String toEmail = user.getEmail();
//            if (toEmail != null) {
//                emailUtil.sendEmailForNewFileActivation_for_admin(toEmail, client);
//            }
//
//            if (client.getHasEmailSent().equals("Yes")
//                    && !StringUtil.isNullOrEmpty(client.getEmail())) {
//                emailUtil.sendEmailForNewFileCreation_for_admin(client);
//            }
//            return ResponseEntity.status(HttpStatus.OK).body(client);
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    }
//
//    @Operation(summary = "This operation is used to find max serial number")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Find max serial number"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "500", description = "Exception occurred while serving the request")})
//    @GetMapping(value = "/findSerialNumber", produces = {"application/json"})
//    public ResponseEntity<Integer> findSerialNumber() {
//        Integer serialNumber = null;
//
//        log.info("Calling service for max serial number");
//        serialNumber = registrationService.getSerialNumber();
//        if (serialNumber == null) {
//            serialNumber = 1;
//
//        } else {
//            serialNumber = serialNumber + 1;
//        }
//        //return ResponseEntity.ok(serialNumber);
//        return ResponseEntity.status(HttpStatus.OK).body(serialNumber);
//    }
//
//    @Operation(summary = "delete client using file reference number") // value="jwtToken" picked from swagger config
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Operation Successful"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "404", description = "Data not found"),
//            @ApiResponse(responseCode = "500", description = "Exception occurs while retrieving the request")})
//    @DeleteMapping(value = "/deleteClient")
//    public ResponseEntity<?> deleteClient(
//            @Parameter(description = "File Reference Number", required = true) @RequestParam(value = "fileReferenceNumber") String fileReferenceNumber) {
//        log.info("Calling service for delete operation");
//        ClientRegistration client = registrationService.findByFileRefNo(fileReferenceNumber);
//        if(client == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        Integer result = registrationService.deleteClient(fileReferenceNumber);
//        if (result > 0) {
//            transactionService.deleteAllTransaction(fileReferenceNumber);
//            installmentService.deleteInstallment(fileReferenceNumber);
//            caseInfoService.deleteCaseInfoForClient(fileReferenceNumber);
//        }
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }
//
//    @Operation(summary = "This operation is used to find a client using file reference")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Find a client"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "404", description = "Data not found"),
//            @ApiResponse(responseCode = "500", description = "Exception occurred while serving the request")})
//    @GetMapping(value = "/findClientUsingFileRefNo/{fileRefNo}", produces = {"application/json"})
//
//    public ResponseEntity<ClientRegistration> findClientUsingFileRefNo(
//            @Parameter(description = "File Reference Number", required = true) @PathVariable(value = "fileRefNo") String fileRefNo) {
//        log.info("Calling service for find client using file reference number");
//        ClientRegistration clientRegistration = registrationService.findByFileRefNo(fileRefNo);
//        if (clientRegistration != null && clientRegistration.getIsActive().equals("Active")) {
//            return ResponseEntity.status(HttpStatus.OK).body(clientRegistration);
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    }
//
//    @Operation(summary = "This operation is used to find a client using file reference and user name")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Find a client"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "404", description = "Data not found"),
//            @ApiResponse(responseCode = "500", description = "Exception occurred while serving the request")})
//    @Produces({"application/json"})
//    @GetMapping(value = "/findClientUsingFRefNoAndUserName/{fileRefNo}/{username}")
//    public ResponseEntity<ClientRegistration> findClientUsingFRefNoAndUserName(
//            @Parameter(description = "File Reference Number", required = true) @PathVariable(value = "fileRefNo") String fileRefNo,
//            @Parameter(description = "User Name", required = true) @PathVariable(value = "username") String username) {
//        log.info("Calling service for find client using file reference and username");
//        ClientRegistration clientRegistration =
//                registrationService.findByFileRefNoAndUserName(fileRefNo, username);
//        if (clientRegistration != null && clientRegistration.getIsActive().equals("Active")) {
//            return ResponseEntity.status(HttpStatus.OK).body(clientRegistration);
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    }
//
//    @Operation(summary = "Find all client- last 200 records")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Operation Successful"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "204", description = "No content"),
//            @ApiResponse(responseCode = "500", description = "Exception occurs while retrieving the request") })
//    @Produces({"application/json"})
//    @GetMapping(value = "/findAllClient")
//    public ResponseEntity<List<ClientRegistration>> findAllClient()
//    {
//        log.info("Calling service to find all client");
//        List<ClientRegistration> clientList = registrationService.findAllClients();
//        return ResponseEntity.status(HttpStatus.OK).body(clientList);
//    }
//
//    @Operation(summary = "Find all client")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Operation Successful"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "204", description = "No content"),
//            @ApiResponse(responseCode = "500", description = "Exception occurs while retrieving the request") })
//    @Produces({"application/json"})
//    @GetMapping(value = "/findAllClientForASpecificUser/{username}")
//    public ResponseEntity<List<ClientRegistration>> findAllClientForASpecificUser(
//            @Parameter(description = "User Name", required = true) @PathVariable(value = "username") String username)
//    {
//        log.info("Calling service to find all client");
//            List<ClientRegistration> clientList =
//                    registrationService.findAllClientsForASpecificUser(username);
//        return ResponseEntity.status(HttpStatus.OK).body(clientList);
//    }
//
//    @Operation(summary = "Find by any name")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Operation Successful"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "204", description = "No content"),
//            @ApiResponse(responseCode = "500", description = "Exception occurs while retrieving the request") })
//    @Produces({"application/json"})
//    @GetMapping(value = "/findClientByAnyName/{anyName}")
//    public ResponseEntity<List<ClientRegistration>> findClientByAnyName(
//            @Parameter(description = "Any Name", required = true) @PathVariable(value = "anyName") String anyName)
//    {
//        log.info("Calling service to find all client using any name");
//        List<ClientRegistration> clientList = registrationService.findByAnyName(anyName);
//        return ResponseEntity.status(HttpStatus.OK).body(clientList);
//    }
//
//    @Operation(summary = "Find by any name with username")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Operation Successful"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "204", description = "No content"),
//            @ApiResponse(responseCode = "500", description = "Exception occurs while retrieving the request") })
//    @Produces({"application/json"})
//    @GetMapping(value = "/findClientByAnyNameAndUsername/{anyName}/{username}")
//    public ResponseEntity<List<ClientRegistration> > findClientByAnyNameAndUsername(
//            @Parameter(description = "Any Name", required = true) @PathVariable(value = "anyName") String anyName,
//            @Parameter(description = "User Name", required = true) @PathVariable(value = "username") String username)
//    {
//        log.info("Calling service to find all client using any name with user name");
//        List<ClientRegistration> clientList =
//                    registrationService.findByAnyNameAndUserName(anyName, username);
//        return ResponseEntity.status(HttpStatus.OK).body(clientList);
//    }
//
//    @Operation(summary = "Find by Fee Earner")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Operation Successful"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "204", description = "No content"),
//            @ApiResponse(responseCode = "500", description = "Exception occurs while retrieving the request") })
//    @Produces({"application/json"})
//    @GetMapping(value = "/findClientByFE/{feeEarner}")
//    public ResponseEntity<List<ClientRegistration>> findClientByFE(
//            @Parameter(description = "Fee Earner Code", required = true) @PathVariable(value = "feeEarner") String feeEarner)
//    {
//        log.info("Calling service to find all client using Fee Earner code");
//        List<ClientRegistration> clientList = registrationService.findByFE(feeEarner);
//        return ResponseEntity.status(HttpStatus.OK).body(clientList);
//    }
//
//    @Operation(summary = "Find by fee earner with username")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Operation Successful"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "204", description = "No content"),
//            @ApiResponse(responseCode = "500", description = "Exception occurs while retrieving the request") })
//    @Produces({"application/json"})
//    @GetMapping(value = "/findClientByFEAndUsername/{feeEarner}/{username}")
//    public ResponseEntity<List<ClientRegistration>> findClientByFEAndUsername(
//            @Parameter(description = "Fee Earner Code", required = true) @PathVariable(value = "feeEarner") String feeEarner,
//            @Parameter(description = "User Name", required = true) @PathVariable(value = "username") String username)
//    {
//        log.info("Calling service to find all client using FE code with user name");
//        List<ClientRegistration> clientList =
//                    registrationService.findByFEAndUserName(feeEarner, username);
//        return ResponseEntity.status(HttpStatus.OK).body(clientList);
//    }
//
//    @Operation(summary = "Find list of client")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Operation Successful"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "204", description = "No content"),
//            @ApiResponse(responseCode = "500", description = "Exception occurs while retrieving the request") })
//    @Produces({"application/json"})
//    @GetMapping(value = "/findClientByFieRef/{fileRefNo}")
//    public ResponseEntity<List<ClientRegistration>> findClientByFieRef(
//            @Parameter(description = "File Reference Number", required = true) @PathVariable(value = "fileRefNo") String fileRefNo)
//    {
//        log.info("Calling service to find list of client using file ref no");
//        List<ClientRegistration> clientList = registrationService.findByFileRefNoForList(fileRefNo);
//        return ResponseEntity.status(HttpStatus.OK).body(clientList);
//    }
//
//    @Operation(summary = "Find by file ref with username")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Operation Successful"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "204", description = "No content"),
//            @ApiResponse(responseCode = "500", description = "Exception occurs while retrieving the request") })
//    @Produces({"application/json"})
//    @GetMapping(value = "/findClientByFileRefAndUsername/{fileRefNo}/{username}")
//    public ResponseEntity<List<ClientRegistration>> findClientByFileRefAndUsername(
//            @Parameter(description = "File Reference Number", required = true) @PathVariable(value = "fileRefNo") String fileRefNo,
//            @Parameter(description = "User Name", required = true) @PathVariable(value = "username") String username)
//    {
//        log.info("Calling service to find all client using any name with user name");
//            List<ClientRegistration> clientList =
//                    registrationService.findByFileRefNoAndUserNameForList(fileRefNo, username);
//        return ResponseEntity.status(HttpStatus.OK).body(clientList);
//    }
//
//    @Operation(description = "Find list of client")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Operation Successful"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "204", description = "No content"),
//            @ApiResponse(responseCode = "500", description = "Exception occurs while retrieving the request") })
//    @Produces({"application/json"})
//    @GetMapping(value = "/findClientByStartDate")
//    public ResponseEntity<List<ClientRegistration>> findClientByStartDate(
//            @Parameter(description = "Start Date (dd/mm/yyyy, 25/12/2022)", required = true) @RequestParam(value = "startDate") String startDate) throws ParseException {
//        log.info("Calling service to find list of client using start date");
//        Date date1 = dateUtil.convertStringDate(startDate);
//        List<ClientRegistration> clientList =
//                    registrationService.findBystartDateForList(date1);
//        return ResponseEntity.status(HttpStatus.OK).body(clientList);
//    }
//
//    @Operation(summary = "Find list of client")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Operation Successful"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "204", description = "No content"),
//            @ApiResponse(responseCode = "500", description = "Exception occurs while retrieving the request") })
//    @Produces({"application/json"})
//    @GetMapping(value = "/findClientByStartDateAndUsername/{username}")
//    public ResponseEntity<List<ClientRegistration>> findClientByStartDateAndUsername(
//            @Parameter(description = "Start Date (dd/mm/yyyy, 25/12/2022)", required = true) @RequestParam(value = "startDate") String startDate,
//            @Parameter(description = "User Name", required = true) @PathVariable(value = "username") String username) throws ParseException{
//        log.info("Calling service to find list of client using file ref no");
//        Date date1 = dateUtil.convertStringDate(startDate);
//        List<ClientRegistration> clientList = registrationService.findBystartDateAndUserNameForList(date1, username);
//        return ResponseEntity.status(HttpStatus.OK).body(clientList);
//    }
//
//    @Operation(summary = "Find list of client")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Operation Successful"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "204", description = "No content"),
//            @ApiResponse(responseCode = "500", description = "Exception occurs while retrieving the request") })
//    @Produces({"application/json"})
//    @GetMapping(value = "/findClientByDob")
//    public ResponseEntity<List<ClientRegistration>> findClientByDob(
//            @Parameter(description = "Birth Date (dd/mm/yyyy, 25/12/2022)", required = true) @RequestParam(value = "dob") String dob) throws ParseException{
//        log.info("Calling service to find list of client using start date");
//        Date date1 = dateUtil.convertStringDate(dob);
//            List<ClientRegistration> clientList = registrationService.findByDOBForList(date1);
//        return ResponseEntity.status(HttpStatus.OK).body(clientList);
//    }
//
//    @Operation(summary = "Find list of client")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Operation Successful"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "204", description = "No content"),
//            @ApiResponse(responseCode = "500", description = "Exception occurs while retrieving the request") })
//    @Produces({"application/json"})
//    @GetMapping(value = "/findClientByDobAndUsername/{username}")
//    public ResponseEntity<List<ClientRegistration>> findClientByDobAndUsername(
//            @Parameter(description = "Birth Date (dd/mm/yyyy, 25/12/2022)", required = true) @RequestParam(value = "dob") String dob,
//            @Parameter(description = "User Name", required = true) @PathVariable(value = "username") String username) throws ParseException{
//       log.info("Calling service to find list of client using file ref no");
//        Date date1 = dateUtil.convertStringDate(dob);
//            List<ClientRegistration> clientList = registrationService.findByDOBAndUserNameForList(date1, username);
//        return ResponseEntity.status(HttpStatus.OK).body(clientList);
//    }
//
//    @Operation(summary = "Find list of client")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Operation Successful"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "204", description = "No content"),
//            @ApiResponse(responseCode = "500", description = "Exception occurs while retrieving the request") })
//    @Produces({"application/json"})
//    @GetMapping(value = "/findClientByBetweenDate")
//    public ResponseEntity<List<ClientRegistration>> findClientByBetweenDate(
//            @Parameter(description = "From Date (dd/mm/yyyy, 25/12/2022)", required = true) @RequestParam(value = "fromDate") String fromDate,
//            @Parameter(description = "To Date (dd/mm/yyyy, 25/12/2022)", required = true) @RequestParam(value = "toDate") String toDate) throws ParseException {
//        log.info("Calling service to find list of client using start date");
//        Date date1 = dateUtil.convertStringDate(fromDate);
//        Date date2 = dateUtil.convertStringDate(toDate);
//            List<ClientRegistration> clientList =
//                    registrationService.findByBetweenDateForList(date1, date2);
//        return ResponseEntity.status(HttpStatus.OK).body(clientList);
//    }
//
//    @Operation(summary = "Find list of client")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Operation Successful"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "204", description = "No content"),
//            @ApiResponse(responseCode = "500", description = "Exception occurs while retrieving the request") })
//    @Produces({"application/json"})
//    @GetMapping(value = "/findClientByBetweenDateAndUsername/{username}")
//    public ResponseEntity<List<ClientRegistration>> findClientByBetweenDateAndUsername(
//            @Parameter(description = "From Date (dd/mm/yyyy, 25/12/2022)", required = true) @RequestParam(value = "fromDate") String fromDate,
//            @Parameter(description = "To Date (dd/mm/yyyy, 25/12/2022)", required = true) @RequestParam(value = "toDate") String toDate,
//            @Parameter(description = "User Name", required = true) @PathVariable(value = "username") String username) throws ParseException {
//
//        log.info("Calling service to find list of client using between start and username");
//        Date date1 = dateUtil.convertStringDate(fromDate);
//        Date date2 = dateUtil.convertStringDate(toDate);
//            List<ClientRegistration> clientList =
//                    registrationService.findByBetweenDateAndUserNameForList(date1, date2, username);
//        return ResponseEntity.status(HttpStatus.OK).body(clientList);
//    }
//
//    @Operation(summary = "Find by category")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Operation Successful"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "204", description = "No content"),
//            @ApiResponse(responseCode = "500", description = "Exception occurs while retrieving the request") })
//    @Produces({"application/json"})
//    @GetMapping(value = "/findClientByCategory/{category}")
//    public ResponseEntity<List<ClientRegistration>> findClientByCategory(
//            @Parameter(description = "Category name", required = true) @PathVariable(value = "category") String category)
//    {
//        log.info("Calling service to find all client using category name");
//            List<ClientRegistration> clientList = registrationService.findByCategoryForList(category);
//        return ResponseEntity.status(HttpStatus.OK).body(clientList);
//    }
//
//    @Operation(summary = "Find by category with username")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Operation Successful"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "204", description = "No content"),
//            @ApiResponse(responseCode = "500", description = "Exception occurs while retrieving the request") })
//    @Produces({"application/json"})
//    @GetMapping(value = "/findClientByCategoryAndUsername/{category}/{username}")
//    public ResponseEntity<List<ClientRegistration>> findClientByCategoryAndUsername(
//            @Parameter(description = "Category name", required = true) @PathVariable(value = "category") String category,
//            @Parameter(description = "User Name", required = true) @PathVariable(value = "username") String username)
//    {
//        log.info("Calling service to find all client using category name with user name");
//            List<ClientRegistration> clientList =
//                    registrationService.findByCategoryAndUserNameForList(category, username);
//        return ResponseEntity.status(HttpStatus.OK).body(clientList);
//    }
//
//    @Operation(summary = "Find by category and FE")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Operation Successful"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "204", description = "No content"),
//            @ApiResponse(responseCode = "500", description = "Exception occurs while retrieving the request") })
//    @Produces({"application/json"})
//    @GetMapping(value = "/findClientByCategoryAndFE/{category}/{fe}")
//    public ResponseEntity<List<ClientRegistration>> findClientByCategoryAndFE(
//            @Parameter(description = "Category name", required = true) @PathVariable(value = "category") String category,
//            @Parameter(description = "Fee Earner Code", required = true) @PathVariable(value = "fe") String fe)
//    {
//        log.info("Calling service to find all client using category name and fe");
//            List<ClientRegistration> clientList = registrationService.findByCategoryAndFEForList(category, fe);
//        return ResponseEntity.status(HttpStatus.OK).body(clientList);
//    }
//
//    @Operation(summary = "Find by category and FE with username")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Operation Successful"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "204", description = "No content"),
//            @ApiResponse(responseCode = "500", description = "Exception occurs while retrieving the request") })
//    @Produces({"application/json"})
//    @GetMapping(value = "/findClientByCategoryAndFEWithUsername/{category}/{fe}/{username}")
//    public ResponseEntity<List<ClientRegistration>> findClientByCategoryAndFEWithUsername(
//            @Parameter(description = "Category name", required = true) @PathVariable(value = "category") String category,
//            @Parameter(description = "Fee Earner Code", required = true) @PathVariable(value = "fe") String fe,
//            @Parameter(description = "User Name", required = true) @PathVariable(value = "username") String username)
//    {
//        log.info("Calling service to find all client using category name and FE with user name");
//            List<ClientRegistration> clientList =
//                    registrationService.findByCategoryAndFEWithUserNameForList(category, fe, username);
//        return ResponseEntity.status(HttpStatus.OK).body(clientList);
//    }
//
//    @Operation(summary = "This operation is used to find last 10 inactive code")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Find last 10 inactive code"),
//            @ApiResponse(responseCode = "400", description = "Bad request"),
//            @ApiResponse(responseCode = "500", description = "Exception occurred while serving the request")})
//    @Produces({"application/json"})
//    @GetMapping(value = "/findLast10ActivationCode")
//    public ResponseEntity<List<ClientRegistration>> findLast10ActivationCode() {
//        log.info("Calling service to find last 10 activation code");
//        List<ClientRegistration> clientList = registrationService.getLast10_inactive_activationCode();
//        return ResponseEntity.status(HttpStatus.OK).body(clientList);
//    }
//}
