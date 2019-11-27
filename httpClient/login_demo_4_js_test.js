client.test("Request executed successfully", function () {
    client.assert(response.status === 200, "Response status is not 200");
    client.log("response.body=" + response.body);
    client.assert(response.body === 'you have been authenticated', "Response body check fail");
});