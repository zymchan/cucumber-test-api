Feature: example
  Scenario Outline: GET
    Given initialize test data keys "<keys>" values "<values>":
    And the headers are:
      | Name | Value |
      |  Content-Type |  application/x-www-form-urlencoded  |
      |   Connection  |          keep-alive             |

    And add header value:
      | Name | Value |
      |  Accept | */*  |
    And the request url is :
    """
    /get
    """
    And the request params are:
      | ParamsKey | ParamsValue |
      |  test1    | test2   |
      |  {test1}  | {test2}|
    When I send a GET request
    Then the response status should be "<code>"
    And the JSON response "<jsonPath>" should equals "<value>"
    And the JSON response "<jsonPath>" should contain "<value>"
    And the JSON response "<jsonPath>" should startWith "<value>"
    And the JSON response "<jsonPath>" should endWith "<value>"

    Examples:
    | keys  | values|  code |jsonPath |value  |
    |<test1><test2>|<456><efg> |200|$.args.test1|test2|




  Scenario Outline: Posting a String
    Given initialize test data keys "<keys>" values "<values>":
    And the request url is :
    """
    /post
    """
    And the postString is:
    """
Duis posuere augue vel cursus pharetra. In luctus a ex nec pretium. Praesent neque quam, tincidunt nec leo eget, rutrum vehicula magna.
Maecenas consequat elementum elit, id semper sem tristique et. Integer pulvinar enim quis consectetur interdum volutpat.
    """
    When I send a POST request

    Examples:
    | keys  | values|
    |<test1><test2>|<456><efg> |



  Scenario Outline: Post Streaming
    Given initialize test data keys "<keys>" values "<values>":
    And the request url is :
    """
    /post
    """
    And the path of postStream file is "<FilePath>"
    When I send a POST request

    Examples:
    | keys  | values| FilePath|
    |<test1><test2>|<456><efg> | src/test/resources/config.properties |




  Scenario Outline:  Posting a File
    Given initialize test data keys "<keys>" values "<values>":
    And the request url is :
    """
    /post
    """
    And the path of file is "<FilePath>"
    When I send a POST request

    Examples:
    | keys  | values| FilePath|
    |<test1><test2>|<456><efg> | src/test/resources/config.properties |





  Scenario Outline: Posting form parameters
    Given initialize test data keys "<keys>" values "<values>":
    And the request url is :
    """
    /post
    """
    And the form data is:
      | key | value |
      |  test1    | test2   |
      |  {test1}  | {test2}|
    When I send a POST request
    Examples:
    | keys  | values|
    |<test1><test2>|<456><efg> |





  Scenario Outline: Posting a multipart request
    Given initialize test data keys "<keys>" values "<values>":
    And the request url is :
    """
    /post
    """
    And the string part is :
      | name | value |
      |  test1    | test2   |
      |  {test1}  | {test2}|
    And the file part is :
      |name|mediaType|filePath|
      |  image | image/png |  website/static/logo-square.png |
    When I send a POST request

    Examples:
    | keys  | values|
    |<test1><test2>|<456><efg> |